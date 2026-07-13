@Override
            protected void doRunCacheCommand(KeycloakSession session, Cache<String, SessionEntityWrapper> cache) {
                int count = getIntArg(2);
                int batchCount = getIntArg(3);
    
                BatchTaskRunner.runInBatches(0, count, batchCount, session.getKeycloakSessionFactory(), (KeycloakSession batchSession, int firstInIteration, int countInIteration) -> {
                    for (int i=0 ; i<countInIteration ; i++) {
                        UserSessionEntity userSession = new UserSessionEntity(KeycloakModelUtils.generateId());
                        userSession.setRealmId((getArg(1)));
    
                        userSession.setLastSessionRefresh(Time.currentTime());
                        cache.put(userSession.getId(), new SessionEntityWrapper(userSession));
                    }
    
                    log.infof("Created '%d' sessions started from offset '%d'", countInIteration, firstInIteration);
                });
    
                log.infof("Created all '%d' sessions", count);
            }
