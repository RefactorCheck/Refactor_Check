public class dubbo_0290 {

        private void applyConfig(final OpenAPI target, OpenAPIConfig config) {
            if (config == null) {
                return;
            }
    
            Info info = target.getInfo();
            setValue(config::getInfoTitle, info::setTitle);
            setValue(config::getInfoDescription, info::setDescription);
            setValue(config::getInfoVersion, info::setVersion);
    
            Contact contact = info.getContact();
            if (contact == null) {
                info.setContact(contact = new Contact());
            }
            setValue(config::getInfoContactName, contact::setName);
            setValue(config::getInfoContactUrl, contact::setUrl);
            setValue(config::getInfoContactEmail, contact::setEmail);
    
            ExternalDocs externalDocs = target.getExternalDocs();
            if (externalDocs == null) {
                target.setExternalDocs(externalDocs = new ExternalDocs());
            }
            setValue(config::getExternalDocsDescription, externalDocs::setDescription);
            setValue(config::getExternalDocsUrl, externalDocs::setUrl);
    
            String[] servers = config.getServers();
            if (servers != null) {
                target.setServers(Arrays.stream(servers).map(Helper::parseServer).collect(Collectors.toList()));
            }
    
            Components components = target.getComponents();
            if (target.getComponents() == null) {
                target.setComponents(components = new Components());
            }
    
            String securityScheme = config.getSecurityScheme();
            if (securityScheme != null) {
                try {
                    if (SECURITY_SCHEMES_TYPE == null) {
                        SECURITY_SCHEMES_TYPE =
                                Components.class.getDeclaredField("securitySchemes").getGenericType();
                    }
                    components.setSecuritySchemes(JsonUtils.toJavaObject(securityScheme, SECURITY_SCHEMES_TYPE));
                } catch (NoSuchFieldException ignored) {
                }
            }
    
            String security = config.getSecurity();
            if (security != null) {
                try {
                    if (SECURITY_TYPE == null) {
                        SECURITY_TYPE = OpenAPI.class.getDeclaredField("security").getGenericType();
                    }
                    target.setSecurity(JsonUtils.toJavaObject(securityScheme, SECURITY_TYPE));
                } catch (NoSuchFieldException ignored) {
                }
            }
        }
}
