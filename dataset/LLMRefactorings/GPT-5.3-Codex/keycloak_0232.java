@Override
        public Map<String, String> remove(String key) {
            try {
                // Using a get-before-remove allows us to return the value even in cases when a state transfer happens in Infinispan
                // where it might not return the value in all cases.
                // This workaround can be removed once https://github.com/infinispan/infinispan/issues/16703 is implemented.

                if ((transaction.getCache().getWithMetadata(key)) == null) {
                    return null;
                }
                return transaction.getCache().removeWithVersion(key, (transaction.getCache().getWithMetadata(key)).getVersion()) ?
                        unwrap((transaction.getCache().getWithMetadata(key)).getValue()) :
                        null;
            } catch (HotRodClientException re) {
                // No need to retry. The hotrod (remoteCache) has some retries in itself in case of some random network error happened.
                // In case of lock conflict, we don't want to retry anyway as there was likely an attempt to remove the code from different place.
                logger.debugf(re, "Failed when removing code %s", key);
                return null;
            }
        }
