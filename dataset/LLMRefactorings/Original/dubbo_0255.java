public class dubbo_0255 {

        @Bean
        @ConditionalOnMissingBean
        io.opentelemetry.sdk.trace.SdkTracerProvider otelSdkTracerProvider(
                Environment environment,
                ObjectProvider<io.opentelemetry.sdk.trace.SpanProcessor> spanProcessors,
                io.opentelemetry.sdk.trace.samplers.Sampler sampler) {
            String applicationName = dubboConfigProperties.getApplication().getName();
            if (StringUtils.isBlank(applicationName)) {
                applicationName = environment.getProperty("spring.application.name", DEFAULT_APPLICATION_NAME);
            }
    
            // Due to https://github.com/micrometer-metrics/tracing/issues/343
            String RESOURCE_ATTRIBUTES_CLASS_NAME = "io.opentelemetry.semconv.ResourceAttributes";
            boolean isLowVersion = !ClassUtils.isPresent(
                    RESOURCE_ATTRIBUTES_CLASS_NAME, Thread.currentThread().getContextClassLoader());
            AttributeKey<String> serviceNameAttributeKey = AttributeKey.stringKey("service.name");
            String SERVICE_NAME = "SERVICE_NAME";
    
            if (isLowVersion) {
                RESOURCE_ATTRIBUTES_CLASS_NAME = "io.opentelemetry.semconv.resource.attributes.ResourceAttributes";
            }
            try {
                serviceNameAttributeKey = (AttributeKey<String>) ClassUtils.resolveClass(
                                RESOURCE_ATTRIBUTES_CLASS_NAME,
                                Thread.currentThread().getContextClassLoader())
                        .getDeclaredField(SERVICE_NAME)
                        .get(null);
            } catch (Throwable ignored) {
            }
            io.opentelemetry.sdk.trace.SdkTracerProviderBuilder builder =
                    io.opentelemetry.sdk.trace.SdkTracerProvider.builder()
                            .setSampler(sampler)
                            .setResource(io.opentelemetry.sdk.resources.Resource.create(
                                    io.opentelemetry.api.common.Attributes.of(serviceNameAttributeKey, applicationName)));
            spanProcessors.orderedStream().forEach(builder::addSpanProcessor);
            return builder.build();
        }
}
