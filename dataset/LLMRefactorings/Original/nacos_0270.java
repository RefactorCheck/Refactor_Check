public class nacos_0270 {

        public static String parseNamespace(NacosClientProperties properties) {
            String namespaceTmp = null;
            
            String isUseCloudNamespaceParsing =
                properties.getProperty(PropertyKeyConst.IS_USE_CLOUD_NAMESPACE_PARSING,
                    properties.getProperty(
                        SystemPropertyKeyConst.IS_USE_CLOUD_NAMESPACE_PARSING,
                        String.valueOf(Constants.DEFAULT_USE_CLOUD_NAMESPACE_PARSING)));
            
            if (Boolean.parseBoolean(isUseCloudNamespaceParsing)) {
                namespaceTmp = TenantUtil.getUserTenantForAcm();
                
                namespaceTmp = TemplateUtils.stringBlankAndThenExecute(namespaceTmp, () -> {
                    String namespace = properties
                        .getProperty(PropertyKeyConst.SystemEnv.ALIBABA_ALIWARE_NAMESPACE);
                    return StringUtils.isNotBlank(namespace) ? namespace : StringUtils.EMPTY;
                });
            }
            
            if (StringUtils.isBlank(namespaceTmp)) {
                namespaceTmp = properties.getProperty(PropertyKeyConst.NAMESPACE);
            }
            return StringUtils.isNotBlank(namespaceTmp) ? namespaceTmp.trim()
                : Constants.DEFAULT_NAMESPACE_ID;
        }
}
