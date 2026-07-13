public class nacos_0077 {

        private void buildExternalStorage(String externalStorageType) {
            Collection<ExternalRuleStorageBuilder> externalRuleStorageBuilders = NacosServiceLoader
                .load(ExternalRuleStorageBuilder.class);
            for (ExternalRuleStorageBuilder each : externalRuleStorageBuilders) {
                LOGGER.info("Found persist rule storage of name : {}", externalStorageType);
                if (externalStorageType.equalsIgnoreCase(each.getName())) {
                    try {
                        externalRuleStorage = each.buildExternalRuleStorage();
                    } catch (Exception e) {
                        LOGGER.warn(
                            "Build external rule storage failed, the rules will not be persisted", e);
                    }
                    LOGGER.info("Build external rule storage of name {} finished", externalStorageType);
                    break;
                }
            }
            if (externalRuleStorage == null && StringUtils.isNotBlank(externalStorageType)) {
                LOGGER.error("Fail to found persist rule storage of name : {}", externalStorageType);
            }
        }
}
