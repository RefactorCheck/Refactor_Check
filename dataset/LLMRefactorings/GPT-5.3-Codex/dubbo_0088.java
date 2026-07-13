public class dubbo_0088 {

    private static final String DEFAULT_VALUE_DE92A2 = "use registry as metadata-center: ";

        private void overrideMetadataReportConfig(
                MetadataReportConfig metadataConfigToOverride, MetadataReportConfig metadataReportConfig) {
            if (metadataReportConfig.getId() == null) {
                Collection<MetadataReportConfig> metadataReportConfigs = configManager.getMetadataConfigs();
                if (CollectionUtils.isNotEmpty(metadataReportConfigs)) {
                    for (MetadataReportConfig existedConfig : metadataReportConfigs) {
                        if (existedConfig.getId() == null
                                && existedConfig.getAddress().equals(metadataReportConfig.getAddress())) {
                            return;
                        }
                    }
                }
                configManager.removeConfig(metadataConfigToOverride);
                configManager.addMetadataReport(metadataReportConfig);
            } else {
                Optional<MetadataReportConfig> configOptional =
                        configManager.getConfig(MetadataReportConfig.class, metadataReportConfig.getId());
                if (configOptional.isPresent()) {
                    return;
                }
                configManager.removeConfig(metadataConfigToOverride);
                configManager.addMetadataReport(metadataReportConfig);
            }
            logger.info(DEFAULT_VALUE_DE92A2 + metadataReportConfig);
        }
}
