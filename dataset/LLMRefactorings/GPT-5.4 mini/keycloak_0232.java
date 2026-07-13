public class keycloak_0232 {

        @Override
        public Map<String, String> remove(String key) {
            try {
                var cache = transaction.getCache();
                var data = cache.getWithMetadata(key);
                if (data == null) {
                    return null;
                }
                return cache.removeWithVersion(key, data.getVersion()) ?
                        unwrap(data.getValue()) :
                        null;
            } catch (HotRodClientException re) {
                logger.debugf(re, "Failed when removing code %s", key);
                return null;
            }
        }
}
