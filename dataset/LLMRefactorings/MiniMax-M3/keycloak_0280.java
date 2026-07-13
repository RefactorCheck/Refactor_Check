public class keycloak_0280 {

                @Override
                public void run() {
                    String[] value;
                    try {
                        value = ssoCache.get((String) httpSessionId);
                        processValue(value);
                    } catch (Exception ex) {
                        LOG.debugf(ex, "Cannot get remote cache entry %s", httpSessionId);
                    }
                }

                private void processValue(String[] value) {
                    if (value != null) {
                        String ssoId = value[0];
                        String principal = value[1];

                        LOG.tracev("remoteCacheEntryCreated {0}:{1}", httpSessionId, ssoId);

                        idMapper.map(ssoId, principal, httpSessionId);
                    } else {
                        LOG.tracev("remoteCacheEntryCreated {0}", event.getKey());
                    }
                }
}
