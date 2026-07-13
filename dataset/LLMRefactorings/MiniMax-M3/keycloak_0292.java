import java.util.List;

import jakarta.persistence.EntityManager;

public class keycloak_0292 {

    @Override
    public boolean removeExpired(KeycloakSession session, String realmId, int currentTime, int maxRemoval, IntConsumer removeCount) {
        var realm = session.realms().getRealm(realmId);
        if (realm == null) {
            return false;
        }
        session.getContext().setRealm(realm);
        var olderTimestamp = currentTime - SessionExpiration.getAuthSessionLifespan(realm);
        var em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        var ids = findExpiredRootAuthSessionIds(em, realmId, olderTimestamp, maxRemoval);
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

    private List<String> findExpiredRootAuthSessionIds(EntityManager em, String realmId, int olderTimestamp, int maxRemoval) {
        return em.createNamedQuery("findExpiredRootAuthSessionIdsByRealm", String.class)
                .setParameter("realmId", realmId)
                .setParameter("timestamp", olderTimestamp)
                .setMaxResults(maxRemoval)
                .getResultList();
    }
}
