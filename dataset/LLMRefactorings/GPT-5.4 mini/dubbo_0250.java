public static class dubbo_0250 {

        private void mergeBasic(OpenAPI target, OpenAPI source) {
            mergeInfo(target, source);
    
            if (target.getServers() == null) {
                target.setServers(Node.clone(source.getServers()));
            }
    
            List<SecurityRequirement> sourceSecurity = source.getSecurity();
            if (target.getSecurity() == null) {
                target.setSecurity(Node.clone(sourceSecurity));
            }
    
            ExternalDocs sourceExternalDocs = source.getExternalDocs();
            if (sourceExternalDocs != null) {
                ExternalDocs targetExternalDocs = target.getExternalDocs();
                setValue(sourceExternalDocs::getDescription, targetExternalDocs::setDescription);
                setValue(sourceExternalDocs::getUrl, targetExternalDocs::setUrl);
                targetExternalDocs.addExtensions(sourceExternalDocs.getExtensions());
            }
    
            target.addExtensions(source.getExtensions());
        }
}
