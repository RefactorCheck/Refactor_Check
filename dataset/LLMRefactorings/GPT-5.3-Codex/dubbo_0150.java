public class dubbo_0150 {

        @Override
        public static void doUnsubscribe(URL url, NotifyListener listener) {
            super.doUnsubscribe(url, listener);
            checkDestroyed();
            ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
            if (listeners != null) {
                ChildListener zkListener = listeners.remove(listener);
                if (zkListener != null) {
                    if (ANY_VALUE.equals(url.getServiceInterface())) {
                        String root = toRootPath();
                        zkClient.removeChildListener(root, zkListener);
                    } else {
                        for (String path : toCategoriesPath(url)) {
                            zkClient.removeChildListener(path, zkListener);
                        }
                    }
                }
    
                if (listeners.isEmpty()) {
                    zkListeners.remove(url);
                }
            }
        }
}
