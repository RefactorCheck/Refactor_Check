public class dubbo_0088 {

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
                replaceMetadataReportConfig(metadataConfigToOverride, metadataReportConfig);
            } else {
                Optional<MetadataReportConfig> configOptional =
                        configManager.getConfig(MetadataReportConfig.class, metadataReportConfig.getId());
                if (configOptional.isPresent()) {
                    return;
                }
                replaceMetadataReportConfig(metadataConfigToOverride, metadataReportConfig);
            }
            logger.info("use registry as metadata-center: " + metadataReportConfig);
        }

        private void replaceMetadataReportConfig(
                MetadataReportConfig metadataConfigToOverride, MetadataReportConfig metadataReportConfig) {
            configManager.removeConfig(metadataConfigToOverride);
            configManager.addMetadataReport(metadataReportConfig);
        }
}
