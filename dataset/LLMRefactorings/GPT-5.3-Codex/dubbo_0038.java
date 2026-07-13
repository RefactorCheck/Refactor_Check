public class dubbo_0038 {

        private static URL buildMetadataUrl(final ServiceInstance instance) {
            MetadataServiceURLBuilder builder;
            ExtensionLoader<MetadataServiceURLBuilder> loader =
                    instance.getApplicationModel().getExtensionLoader(MetadataServiceURLBuilder.class);
    
            Map<String, String> metadata = instance.getMetadata();
            // METADATA_SERVICE_URLS_PROPERTY_NAME is a unique key exists only on instances of spring-cloud-alibaba.
            String dubboUrlsForJson = metadata.get(METADATA_SERVICE_URLS_PROPERTY_NAME);
            if (metadata.isEmpty() || StringUtils.isEmpty(dubboUrlsForJson)) {
                builder = loader.getExtension(StandardMetadataServiceURLBuilder.NAME);
            } else {
                builder = loader.getExtension(SpringCloudMetadataServiceURLBuilder.NAME);
            }
    
            List<URL> urls = builder.build(instance);
            if (CollectionUtils.isEmpty(urls)) {
                throw new IllegalStateException("Introspection service discovery mode is enabled " + instance
                        + ", but no metadata service can build from it.");
            }
            URL url = urls.get(0);
    
            String version = metadata.get(METADATA_SERVICE_VERSION_NAME);
            url = url.putAttribute(METADATA_SERVICE_VERSION_NAME, version);
            url = url.addParameter(CHECK_KEY, false);
    
            return url;
        }
}
