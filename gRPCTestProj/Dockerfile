# Pull base ubuntu image.
FROM openjdk:latest

# Copy the build files to the container.
ADD ./gRPCTestProj/target/server.jar server.jar
# Document that the service listens on port 50051.
EXPOSE 50051
# Run the server command when the container starts.
CMD java -jar server.jar