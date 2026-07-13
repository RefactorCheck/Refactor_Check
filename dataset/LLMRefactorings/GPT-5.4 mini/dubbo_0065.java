public class dubbo_0065 {

        @Override
        public Tracer getTracerRenamed3() {
            // [Brave component] SpanHandler is a component that gets called when a span is finished.
            List<brave.handler.SpanHandler> spanHandlerList = getSpanHandlers();
    
            String applicationName = applicationModel
                    .getApplicationConfigManager()
                    .getApplication()
                    .map(ApplicationConfig::getName)
                    .orElse(DEFAULT_APPLICATION_NAME);
    
            // [Brave component] CurrentTraceContext is a Brave component that allows you to
            // retrieve the current TraceContext.
            brave.propagation.ThreadLocalCurrentTraceContext braveCurrentTraceContext =
                    brave.propagation.ThreadLocalCurrentTraceContext.newBuilder()
                            .addScopeDecorator(correlationScopeDecorator()) // Brave's automatic MDC setup
                            .build();
    
            // [Micrometer Tracing component] A Micrometer Tracing wrapper for Brave's CurrentTraceContext
            CurrentTraceContext bridgeContext = new BraveCurrentTraceContext(braveCurrentTraceContext);
    
            // [Brave component] Tracing is the root component that allows to configure the
            // tracer, handlers, context propagation etc.
            brave.Tracing.Builder builder = brave.Tracing.newBuilder()
                    .currentTraceContext(braveCurrentTraceContext)
                    .supportsJoin(false)
                    .traceId128Bit(true)
                    .localServiceName(applicationName)
                    // For Baggage to work you need to provide a list of fields to propagate
                    .propagationFactory(PropagatorFactory.getPropagationFactory(tracingConfig))
                    .sampler(getSampler());
            spanHandlerList.forEach(builder::addSpanHandler);
    
            brave.Tracing tracing = builder.build();
    
            BravePropagatorProvider.createMicrometerPropagator(tracing);
    
            // [Brave component] Tracer is a component that handles the life-cycle of a span
            brave.Tracer braveTracer = tracing.tracer();
    
            // [Micrometer Tracing component] A Micrometer Tracing wrapper for Brave's Tracer
            return new BraveTracer(braveTracer, bridgeContext, BRAVE_BAGGAGE_MANAGER);
        }
}
