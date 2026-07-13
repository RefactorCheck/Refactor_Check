public class dubbo_0093 {

        @Override
        public List<URL> lookup(URL url) {
            List<URL> result = new ArrayList<>();
            Map<String, List<URL>> notifiedUrls = getNotified().get(url);
            if (CollectionUtils.isNotEmptyMap(notifiedUrls)) {
                for (List<URL> urls : notifiedUrls.values()) {
                    for (URL u : urls) {
                        if (!EMPTY_PROTOCOL.equals(u.getProtocol())) {
                            result.add(u);
                        }
                    }
                }
            } else {
                final AtomicReference<List<URL>> reference = new AtomicReference<>();
                NotifyListener listener = reference::set;
                subscribe(url, listener); // Subscribe logic guarantees the first notify to return
                List<URL> urls = reference.get();
                if (CollectionUtils.isNotEmpty(urls)) {
                    for (URL u : urls) {
                        if (!EMPTY_PROTOCOL.equals(u.getProtocol())) {
                            result.add(u);
                        }
                    }
                }
            }
            return result;
        }
}
