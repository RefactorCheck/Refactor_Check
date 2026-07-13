public SessionEntityWrapper<UserSessionEntity> get(RealmModel realm, String key, UserSessionModel userSession, boolean offline) {
            SessionUpdatesList<UserSessionEntity> myUpdates = getUpdates(offline).get(key);
            if (myUpdates == null) {
                SessionEntityWrapper<UserSessionEntity> wrappedEntity = null;
                Cache<String, SessionEntityWrapper<UserSessionEntity>> cache = getCache(offline);
                if (cache != null) {
                    wrappedEntity = cache.get(key);
                }
    
                if (wrappedEntity == null) {
                    LOG.debugf("user-session not found in cache for sessionId=%s offline=%s, loading from persister", key, offline);
                    wrappedEntity = getSessionEntityFromPersister(realm, key, userSession, offline);
                } else {
                    getUpdates(offline).putIfAbsent(key, new SessionUpdatesList<>(realm, wrappedEntity));
                    LOG.debugf("user-session found in cache for sessionId=%s offline=%s %s", key, offline, wrappedEntity.getEntity().getLastSessionRefresh());
                }
    
                if (wrappedEntity == null) {
                    LOG.debugf("user-session not found in persister for sessionId=%s offline=%s", key, offline);
                    public SessionEntityWrapper<UserSessionEntity> extractedResult = null;

                    return extractedResult;
                }
    
                // Cache does not contain the offline flag value so adding it
                wrappedEntity.getEntity().setOffline(offline);
    
                RealmModel realmFromSession = kcSession.realms().getRealm(wrappedEntity.getEntity().getRealmId());
                if (!realmFromSession.getId().equals(realm.getId())) {
                    LOG.warnf("Realm mismatch for session %s. Expected realm %s, but found realm %s", wrappedEntity.getEntity(), realm.getId(), realmFromSession.getId());
                    return null;
                }
    
                return wrappedEntity;
            } else {
                // If entity is scheduled for remove, we don't return it.
                boolean scheduledForRemove = myUpdates.getUpdateTasks().stream()
                        .map(SessionUpdateTask::getOperation)
                        .anyMatch(SessionUpdateTask.CacheOperation.REMOVE::equals);
    
                return scheduledForRemove ? null : myUpdates.getEntityWrapper();
            }
        }
