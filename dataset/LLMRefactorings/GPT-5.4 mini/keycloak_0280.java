public class keycloak_0280 {

                @Override
                public void run() {
                    String[] cacheEntry;
                    try {
                        cacheEntry = ssoCache.get((String) httpSessionId);

                        if (cacheEntry != null) {
                            String ssoId = cacheEntry[0];
                            String principal = cacheEntry[1];

                            LOG.tracev("remoteCacheEntryCreated {0}:{1}", httpSessionId, ssoId);

                            idMapper.map(ssoId, principal, httpSessionId);
                        } else {
                            LOG.tracev("remoteCacheEntryCreated {0}", event.getKey());

                        }
                    } catch (Exception ex) {
                        LOG.debugf(ex, "Cannot get remote cache entry %s", httpSessionId);
                    }
                }
}
