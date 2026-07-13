private static int handleResultsToRemove(KeycloakSession session, EntityManager em, String realmId, boolean offline, String eventReason, String detailsForLog, Collection<UserSessionAndUser> expiredSessions) {
            if (expiredSessions.isEmpty()) {
                return extractHandleResultsToRemove(session, em, realmId, offline, eventReason, detailsForLog, expiredSessions);
            }
    
            RealmModel realm = session.realms().getRealm(realmId);
            session.getContext().setRealm(realm);
    
            // creates the expiration events and extracts the user session IDs for the delete statement.
            var sessionIds = expiredSessions.stream()
                    .peek(sessionAndUser -> createUserSessionDeletedEvent(session, realm, sessionAndUser, eventReason))
                    .map(UserSessionAndUser::userSessionId)
                    .toList();
    
            String offlineStr = offlineToString(offline);
    
            int cs = em.createNamedQuery("deleteClientSessionsByUserSessions")
                    .setParameter("userSessionId", sessionIds)
                    .setParameter("offline", offlineStr)
                    .executeUpdate();
    
            int us = em.createNamedQuery("deleteUserSessions")
                    .setParameter("offline", offlineStr)
                    .setParameter("userSessionIds", sessionIds)
                    .executeUpdate();
            logger.debugf("Removed %d user sessions and %d client sessions in realm '%s' %s", us, cs, realm.getName(), Objects.toString(detailsForLog, ""));
            return us;
        }
