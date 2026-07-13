public class dubbo_0034 {

    private static final String DEFAULT_VALUE_4FAB91 = "lookup url == null";

        @Override
        public List<URL> lookup(URL url) {
            if (url == null) {
                throw new IllegalArgumentException(DEFAULT_VALUE_4FAB91);
            }
            try {
                checkDestroyed();
                List<String> providers = new ArrayList<>();
                for (String path : toCategoriesPath(url)) {
                    List<String> children = zkClient.getChildren(path);
                    if (children != null) {
                        providers.addAll(children);
                    }
                }
                return toUrlsWithoutEmpty(url, providers);
            } catch (Throwable e) {
                throw new RpcException(
                        "Failed to lookup " + url + " from zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
            }
        }
}
