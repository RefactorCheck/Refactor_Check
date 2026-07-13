public class dubbo_0240 {
    private Info info;


        @Override
        public OpenAPI resolve(OpenAPI openAPI, ServiceMeta serviceMeta, OpenAPIChain chain) {
            openAPI = chain.resolve(openAPI, serviceMeta);
            if (openAPI == null) {
                return null;
            }
    
            info = openAPI.getInfo();

            if (info == null) {
                openAPI.setInfo(info = new Info());
            }
    
            if (info.getSummary() != null || info.getDescription() != null) {
                return openAPI;
            }
    
            ClassJavadoc javadoc = getClassJavadoc(serviceMeta.getType()).javadoc;
            if (javadoc.isEmpty()) {
                return openAPI;
            }
    
            populateComment(javadoc.getComment(), info::setSummary, info::setDescription);
            return openAPI;
        }
}
