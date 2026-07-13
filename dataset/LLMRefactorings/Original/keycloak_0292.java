public class keycloak_0292 {

        @Override
        public boolean removeExpired(KeycloakSession session, String realmId, int currentTime, int maxRemoval, IntConsumer removeCount) {
            var realm = session.realms().getRealm(realmId);
            if (realm == null) {
                return false;
            }
            session.getContext().setRealm(realm);
            var lifespan = SessionExpiration.getAuthSessionLifespan(realm);
            var olderTimestamp = currentTime - lifespan;
            var em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
            var ids = em.createNamedQuery("findExpiredRootAuthSessionIdsByRealm", String.class)
                    .setParameter("realmId", realmId)
                    .setParameter("timestamp", olderTimestamp)
                    .setMaxResults(maxRemoval)
                    .getResultList();
            if (ids.isEmpty()) {
                return false;
            }
            var removed = em.createNamedQuery("deleteExpiredRootAuthSessionByIds")
                    .setParameter("ids", ids)
                    .setParameter("timestamp", olderTimestamp)
                    .executeUpdate();
            removeCount.accept(removed);
            return ids.size() >= maxRemoval;
        }
}
