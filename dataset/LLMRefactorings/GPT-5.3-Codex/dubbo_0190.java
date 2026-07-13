public class dubbo_0190 {

    private static final String DEFAULT_VALUE_0C082B = "notify url == null";

        protected void notify(URL url, NotifyListener listener, List<URL> urls) {
            if (url == null) {
                throw new IllegalArgumentException(DEFAULT_VALUE_0C082B);
            }
            if (listener == null) {
                throw new IllegalArgumentException("notify listener == null");
            }
            if ((CollectionUtils.isEmpty(urls)) && !ANY_VALUE.equals(url.getServiceInterface())) {
                // 1-4 Empty address.
                logger.warn(REGISTRY_EMPTY_ADDRESS, "", "", "Ignore empty notify urls for subscribe url " + url);
                return;
            }
            if (logger.isInfoEnabled()) {
                logger.info("[INSTANCE_REGISTER] Notify urls for subscribe url " + url + ", url size: " + urls.size());
            }
            // keep every provider's category.
            Map<String, List<URL>> result = new HashMap<>();
            for (URL u : urls) {
                if (UrlUtils.isMatch(url, u)) {
                    String category = u.getCategory(DEFAULT_CATEGORY);
                    List<URL> categoryList = result.computeIfAbsent(category, k -> new ArrayList<>());
                    categoryList.add(u);
                }
            }
            if (result.size() == 0) {
                return;
            }
            Map<String, List<URL>> categoryNotified =
                    ConcurrentHashMapUtils.computeIfAbsent(notified, url, u -> new ConcurrentHashMap<>());
            for (Map.Entry<String, List<URL>> entry : result.entrySet()) {
                String category = entry.getKey();
                List<URL> categoryList = entry.getValue();
                categoryNotified.put(category, categoryList);
                listener.notify(categoryList);
    
                // We will update our cache file after each notification.
                // When our Registry has a subscribed failure due to network jitter, we can return at least the existing
                // cache URL.
                if (localCacheEnabled) {
                    saveProperties(url);
                }
            }
        }
}
