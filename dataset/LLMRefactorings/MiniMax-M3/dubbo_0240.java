public class dubbo_0240 {

        @Override
        public OpenAPI resolve(OpenAPI openAPI, ServiceMeta serviceMeta, OpenAPIChain chain) {
            openAPI = chain.resolve(openAPI, serviceMeta);
            if (openAPI == null) {
                return null;
            }
    
            Info info = openAPI.getInfo();
            if (info == null) {
                openAPI.setInfo(info = new Info());
            }
    
            if (info.getSummary() != null || info.getDescription() != null) {
                return openAPI;
            }
    
            applyJavadoc(info, getClassJavadoc(serviceMeta.getType()).javadoc);
            return openAPI;
        }

        private void applyJavadoc(Info info, ClassJavadoc javadoc) {
            if (javadoc.isEmpty()) {
                return;
            }
            populateComment(javadoc.getComment(), info::setSummary, info::setDescription);
        }
}
