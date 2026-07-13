public class keycloak_0067 {

        @Override
        public void removeExpired(RealmModel realm) {
            final RealmExpiration expiration = RealmExpiration.fromRealm(realm);
            final int currentTime = Time.currentTime();
            final KeycloakSessionFactory sessionFactory = session.getKeycloakSessionFactory();

            UserSessionExpirationLogic.expireOfflineSessions(sessionFactory, realm, currentTime, expiration, expirationBatch);

            boolean persistentSessionsEnabled = MultiSiteUtils.isPersistentSessionsEnabled();
            if (!persistentSessionsEnabled) {
                return;
            }

            // The offline sessions do not have remember_me flag. We do not waste time migrating them.
            UserSessionExpirationLogic.migrateRememberMe(sessionFactory, realm, expiration, currentTime, expirationBatch);

            UserSessionExpirationLogic.expireRegularSessions(sessionFactory, realm, currentTime, expiration, false, expirationBatch);
            if (realm.isRememberMe()) {
                UserSessionExpirationLogic.expireRegularSessions(sessionFactory, realm, currentTime, expiration, true, expirationBatch);
            } else {
                UserSessionExpirationLogic.deleteInvalidSessions(sessionFactory, realm, expirationBatch);
            }
        }
}
