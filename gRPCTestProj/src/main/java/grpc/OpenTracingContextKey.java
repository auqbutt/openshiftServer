/*
 * Copyright 2017-2020 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package grpc;

import io.grpc.Context;
import io.opentracing.Span;
import io.opentracing.SpanContext;

/**
 * A {@link Context} key for the current OpenTracing trace state.
 *
 * <p>Can be used to get the active span, or to set the active span for a scoped unit of work. See
 * the <a href="../../../../../../README.rst">grpc-java OpenTracing docs</a> for use cases and
 * examples.
 */
public class OpenTracingContextKey {

  public static final String KEY_NAME = "io.opentracing.active-span";
  public static final String KEY_CONTEXT_NAME = "io.opentracing.active-span-context";
  private static final Context.Key<Span> key = Context.key(KEY_NAME);
  private static final Context.Key<SpanContext> keyContext = Context.key(KEY_CONTEXT_NAME);

  /**
   * Retrieves the active span.
   *
   * @return the active span for the current request
   */
  public static Span activeSpan() {
    return key.get();
  }

  /**
   * Retrieves the span key.
   *
   * @return the OpenTracing context key
   */
  public static Context.Key<Span> getKey() {
    return key;
  }

  /**
   * Retrieves the span context key.
   *
   * @return the OpenTracing context key for span context
   */
  public static Context.Key<SpanContext> getSpanContextKey() {
    return keyContext;
  }

  public static SpanContext activeSpanContext() {
    return keyContext.get();
  }
}
