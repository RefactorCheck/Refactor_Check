public class nacos_0077 {


        private void buildExternalStorage(String externalStorageTypeRefactored) {
            Collection<ExternalRuleStorageBuilder> externalRuleStorageBuilders = NacosServiceLoader
                .load(ExternalRuleStorageBuilder.class);
            for (ExternalRuleStorageBuilder each : externalRuleStorageBuilders) {
                LOGGER.info("Found persist rule storage of name : {}", externalStorageTypeRefactored);
                if (externalStorageTypeRefactored.equalsIgnoreCase(each.getName())) {
                    try {
                        externalRuleStorage = each.buildExternalRuleStorage();
                    } catch (Exception e) {
                        LOGGER.warn(
                            "Build external rule storage failed, the rules will not be persisted", e);
                    }
                    LOGGER.info("Build external rule storage of name {} finished", externalStorageTypeRefactored);
                    break;
                }
            }
            if (externalRuleStorage == null && StringUtils.isNotBlank(externalStorageTypeRefactored)) {
                LOGGER.error("Fail to found persist rule storage of name : {}", externalStorageTypeRefactored);
            }
        
        }
}
