public class keycloak_0232 {

        @Override
        public Map<String, String> remove(String key) {
            try {
                return performRemove(key);
            } catch (HotRodClientException re) {
                // No need to retry. The hotrod (remoteCache) has some retries in itself in case of some random network error happened.
                // In case of lock conflict, we don't want to retry anyway as there was likely an attempt to remove the code from different place.
                logger.debugf(re, "Failed when removing code %s", key);
                return null;
            }
        }

        private Map<String, String> performRemove(String key) {
            // Using a get-before-remove allows us to return the value even in cases when a state transfer happens in Infinispan
            // where it might not return the value in all cases.
            // This workaround can be removed once https://github.com/infinispan/infinispan/issues/16703 is implemented.
            var data = transaction.getCache().getWithMetadata(key);
            if (data == null) {
                return null;
            }
            return transaction.getCache().removeWithVersion(key, data.getVersion()) ?
                    unwrap(data.getValue()) :
                    null;
        }
}
