public class dubbo_0038 {

        private static URL buildMetadataUrl(ServiceInstance instance) {
            MetadataServiceURLBuilder builder = selectMetadataServiceURLBuilder(instance);

            List<URL> urls = builder.build(instance);
            if (CollectionUtils.isEmpty(urls)) {
                throw new IllegalStateException("Introspection service discovery mode is enabled " + instance
                        + ", but no metadata service can build from it.");
            }
            URL url = urls.get(0);

            String version = instance.getMetadata().get(METADATA_SERVICE_VERSION_NAME);
            url = url.putAttribute(METADATA_SERVICE_VERSION_NAME, version);
            url = url.addParameter(CHECK_KEY, false);

            return url;
        }

        private static MetadataServiceURLBuilder selectMetadataServiceURLBuilder(ServiceInstance instance) {
            ExtensionLoader<MetadataServiceURLBuilder> loader =
                    instance.getApplicationModel().getExtensionLoader(MetadataServiceURLBuilder.class);

            Map<String, String> metadata = instance.getMetadata();
            // METADATA_SERVICE_URLS_PROPERTY_NAME is a unique key exists only on instances of spring-cloud-alibaba.
            String dubboUrlsForJson = metadata.get(METADATA_SERVICE_URLS_PROPERTY_NAME);
            if (metadata.isEmpty() || StringUtils.isEmpty(dubboUrlsForJson)) {
                return loader.getExtension(StandardMetadataServiceURLBuilder.NAME);
            } else {
                return loader.getExtension(SpringCloudMetadataServiceURLBuilder.NAME);
            }
        }
}
