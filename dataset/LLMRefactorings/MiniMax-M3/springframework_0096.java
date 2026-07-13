public class springframework_0096 {

    protected Mono<Resource> getResource(String resourcePath, Resource location) {
        try {
            Resource resource = ResourceHandlerUtils.createRelativeResource(location, resourcePath);
            if (resource.isReadable()) {
                if (checkResource(resource, location)) {
                    return Mono.just(resource);
                }
                else if (logger.isWarnEnabled()) {
                    logger.warn(buildResourceNotAllowedMessage(resourcePath, resource, location));
                }
            }
            return Mono.empty();
        }
        catch (IOException ex) {
            if (logger.isDebugEnabled()) {
                String error = "Skip location [" + location + "] due to error";
                if (logger.isTraceEnabled()) {
                    logger.trace(error, ex);
                }
                else {
                    logger.debug(error + ": " + ex.getMessage());
                }
            }
            return Mono.error(ex);
        }
    }

    private String buildResourceNotAllowedMessage(String resourcePath, Resource resource, Resource location) {
        Resource[] allowed = getAllowedLocations();
        return LogFormatUtils.formatValue(
                "Resource path \"" + resourcePath + "\" was successfully resolved " +
                        "but resource \"" + resource + "\" is neither under the " +
                        "current location \"" + location + "\" nor under any of the " +
                        "allowed locations " + (allowed != null ? Arrays.asList(allowed) : "[]"), -1, true);
    }
}
