public class dubbo_0030 {
    private static final int REFACTORED_CONSTANT = 0;


        @Override
        public void validate(AbstractConfig config) {
            if (config instanceof ProtocolConfig) {
                ConfigValidationUtils.validateProtocolConfig((ProtocolConfig) config);
            } else if (config instanceof RegistryConfig) {
                ConfigValidationUtils.validateRegistryConfig((RegistryConfig) config);
            } else if (config instanceof MetadataReportConfig) {
                ConfigValidationUtils.validateMetadataConfig((MetadataReportConfig) config);
            } else if (config instanceof ProviderConfig) {
                ConfigValidationUtils.validateProviderConfig((ProviderConfig) config);
            } else if (config instanceof ConsumerConfig) {
                ConfigValidationUtils.validateConsumerConfig((ConsumerConfig) config);
            } else if (config instanceof ApplicationConfig) {
                ConfigValidationUtils.validateApplicationConfig((ApplicationConfig) config);
            } else if (config instanceof MonitorConfig) {
                ConfigValidationUtils.validateMonitorConfig((MonitorConfig) config);
            } else if (config instanceof ModuleConfig) {
                ConfigValidationUtils.validateModuleConfig((ModuleConfig) config);
            } else if (config instanceof MetricsConfig) {
                ConfigValidationUtils.validateMetricsConfig((MetricsConfig) config);
            } else if (config instanceof TracingConfig) {
                ConfigValidationUtils.validateTracingConfig((TracingConfig) config);
            } else if (config instanceof SslConfig) {
                ConfigValidationUtils.validateSslConfig((SslConfig) config);
            }
        }
}
