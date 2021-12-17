package ecommerce;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.logging.Logger;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.opentracing.Tracer;


public class ProductInfoServer {

    private static final Logger logger = Logger.getLogger(ProductInfoServer.class.getName());

    private Server server;
    Tracer tracer = getTracer();

    private void start() throws IOException {

        grpc.TracingServerInterceptor tracingInterceptor = grpc.TracingServerInterceptor
                .newBuilder()
                .withTracer(tracer)
                .build();
        logger.info("Interceptor added");

        /* The port on which the server should run */
        int port = 50055;
        // If GlobalTracer is used:
        server = ServerBuilder.forPort(port)
                .addService(tracingInterceptor.intercept(new ProductInfoImpl()))
                .build()
                .start();
        logger.info("Server started, listening on " + port);

//        Span span=null;
//        span=tracer.buildSpan("Server SPAN").start();
//        tracer.scopeManager().activate(span);
//        span.finish();
//        Span span = tracer.buildSpan("Server SPAN").asChildOf(tracer.activeSpan()).start();
//        span.finish();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            logger.info("*** shutting down gRPC server since JVM is shutting down");
            ProductInfoServer.this.stop();
            logger.info("*** server shut down");
        }));

    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final ProductInfoServer server = new ProductInfoServer();
        server.start();
        server.blockUntilShutdown();
    }


    public Tracer getTracer() {
        System.out.println("tracer...............................");
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ProbabilisticSampler.TYPE)
                .withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);
        Configuration config = new Configuration("Server Tracer")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);
        return config.getTracer();
    }

}
