public class keycloak_0292 {

        @Override
        public boolean removeExpired(KeycloakSession session, String realmId, int currentTime, int maxRemoval, IntConsumer removeCount) {
            var realm = session.realms().getRealm(realmId);
            if (realm == null) {
                return false;
            }
            session.getContext().setRealm(realm);
            var lifespan = SessionExpiration.getAuthSessionLifespan(realm);
            var expiredBefore = currentTime - lifespan;
            var em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
            var expiredRootAuthSessionIds = em.createNamedQuery("findExpiredRootAuthSessionIdsByRealm", String.class)
                    .setParameter("realmId", realmId)
                    .setParameter("timestamp", expiredBefore)
                    .setMaxResults(maxRemoval)
                    .getResultList();
            if (expiredRootAuthSessionIds.isEmpty()) {
                return false;
            }
            var removedCount = em.createNamedQuery("deleteExpiredRootAuthSessionByIds")
                    .setParameter("ids", expiredRootAuthSessionIds)
                    .setParameter("timestamp", expiredBefore)
                    .executeUpdate();
            removeCount.accept(removedCount);
            return expiredRootAuthSessionIds.size() >= maxRemoval;
        }
}
