public class dubbo_0093 {

        @Override
        public List<URL> lookup(URL url) {
            List<URL> result = new ArrayList<>();
            Map<String, List<URL>> notifiedUrls = getNotified().get(url);
            if (CollectionUtils.isNotEmptyMap(notifiedUrls)) {
                for (List<URL> urls : notifiedUrls.values()) {
                    addValidUrls(urls, result);
                }
            } else {
                final AtomicReference<List<URL>> reference = new AtomicReference<>();
                NotifyListener listener = reference::set;
                subscribe(url, listener); // Subscribe logic guarantees the first notify to return
                List<URL> urls = reference.get();
                if (CollectionUtils.isNotEmpty(urls)) {
                    addValidUrls(urls, result);
                }
            }
            return result;
        }

        private void addValidUrls(List<URL> urls, List<URL> result) {
            for (URL u : urls) {
                if (!EMPTY_PROTOCOL.equals(u.getProtocol())) {
                    result.add(u);
                }
            }
        }
}
