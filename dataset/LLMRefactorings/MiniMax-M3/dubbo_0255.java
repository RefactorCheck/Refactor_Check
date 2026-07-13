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

            AttributeKey<String> serviceNameAttributeKey = resolveServiceNameAttributeKey();
            io.opentelemetry.sdk.trace.SdkTracerProviderBuilder builder =
                    io.opentelemetry.sdk.trace.SdkTracerProvider.builder()
                            .setSampler(sampler)
                            .setResource(io.opentelemetry.sdk.resources.Resource.create(
                                    io.opentelemetry.api.common.Attributes.of(serviceNameAttributeKey, applicationName)));
            spanProcessors.orderedStream().forEach(builder::addSpanProcessor);
            return builder.build();
        }

        private AttributeKey<String> resolveServiceNameAttributeKey() {
            // Due to https://github.com/micrometer-metrics/tracing/issues/343
            String resourceAttributesClassName = "io.opentelemetry.semconv.ResourceAttributes";
            String serviceName = "SERVICE_NAME";
            AttributeKey<String> serviceNameAttributeKey = AttributeKey.stringKey("service.name");
            boolean isLowVersion = !ClassUtils.isPresent(
                    resourceAttributesClassName, Thread.currentThread().getContextClassLoader());
            if (isLowVersion) {
                resourceAttributesClassName = "io.opentelemetry.semconv.resource.attributes.ResourceAttributes";
            }
            try {
                serviceNameAttributeKey = (AttributeKey<String>) ClassUtils.resolveClass(
                                resourceAttributesClassName,
                                Thread.currentThread().getContextClassLoader())
                        .getDeclaredField(serviceName)
                        .get(null);
            } catch (Throwable ignored) {
            }
            return serviceNameAttributeKey;
        }
}
