public class keycloak_0130 {

        public SessionEntityWrapper<UserSessionEntity> get(RealmModel realm, String key, UserSessionModel userSession, boolean offline) {
            SessionUpdatesList<UserSessionEntity> myUpdates = getUpdates(offline).get(key);
            if (myUpdates == null) {
                SessionEntityWrapper<UserSessionEntity> sessionEntity = null;
                Cache<String, SessionEntityWrapper<UserSessionEntity>> cache = getCache(offline);
                if (cache != null) {
                    sessionEntity = cache.get(key);
                }

                if (sessionEntity == null) {
                    LOG.debugf("user-session not found in cache for sessionId=%s offline=%s, loading from persister", key, offline);
                    sessionEntity = getSessionEntityFromPersister(realm, key, userSession, offline);
                } else {
                    getUpdates(offline).putIfAbsent(key, new SessionUpdatesList<>(realm, sessionEntity));
                    LOG.debugf("user-session found in cache for sessionId=%s offline=%s %s", key, offline, sessionEntity.getEntity().getLastSessionRefresh());
                }

                if (sessionEntity == null) {
                    LOG.debugf("user-session not found in persister for sessionId=%s offline=%s", key, offline);
                    return null;
                }

                // Cache does not contain the offline flag value so adding it
                sessionEntity.getEntity().setOffline(offline);

                RealmModel realmFromSession = kcSession.realms().getRealm(sessionEntity.getEntity().getRealmId());
                if (!realmFromSession.getId().equals(realm.getId())) {
                    LOG.warnf("Realm mismatch for session %s. Expected realm %s, but found realm %s", sessionEntity.getEntity(), realm.getId(), realmFromSession.getId());
                    return null;
                }

                return sessionEntity;
            } else {
                // If entity is scheduled for remove, we don't return it.
                boolean scheduledForRemove = myUpdates.getUpdateTasks().stream()
                        .map(SessionUpdateTask::getOperation)
                        .anyMatch(SessionUpdateTask.CacheOperation.REMOVE::equals);

                return scheduledForRemove ? null : myUpdates.getEntityWrapper();
            }
        }
}
