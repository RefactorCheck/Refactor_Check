public class dubbo_0034 {

    @Override
    public List<URL> lookup(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("lookup url == null");
        }
        try {
            checkDestroyed();
            List<String> providers = collectProviders(url);
            return toUrlsWithoutEmpty(url, providers);
        } catch (Throwable e) {
            throw new RpcException(
                    "Failed to lookup " + url + " from zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
        }
    }

    private List<String> collectProviders(URL url) {
        List<String> providers = new ArrayList<>();
        for (String path : toCategoriesPath(url)) {
            List<String> children = zkClient.getChildren(path);
            if (children != null) {
                providers.addAll(children);
            }
        }
        return providers;
    }
}
