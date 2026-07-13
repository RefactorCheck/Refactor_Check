public class dubbo_0271 {

        protected void recover() throws Exception {
            // register
            Set<URL> recoverRegistered = new HashSet<>(getRegistered());
            if (!recoverRegistered.isEmpty()) {
                if (logger.isInfoEnabled()) {
                    logger.info("Recover register url " + recoverRegistered);
                }
                for (URL url : recoverRegistered) {
                    register(url);
                }
            }
            // subscribe
            Map<URL, Set<NotifyListener>> recoverSubscribed = new HashMap<>(getSubscribed());
            if (!recoverSubscribed.isEmpty()) {
                if (logger.isInfoEnabled()) {
                    logger.info("Recover subscribe url " + recoverSubscribed.keySet());
                }
                for (Map.Entry<URL, Set<NotifyListener>> entry : recoverSubscribed.entrySet()) {
                    URL url = entry.getKey();
                    for (NotifyListener listener : entry.getValue()) {
                        subscribe(url, listener);
                    }
                }
            }
        }
}
