public class keycloak_0094 {

        private List<UserSessionModel> logoutOldestSessions(List<UserSessionModel> userSessions, long limit, EventBuilder eventBuilder) {
            long numberOfSessionsThatNeedToBeLoggedOut = getNumberOfSessionsThatNeedToBeLoggedOut(userSessions.size(), limit);
    
            if (numberOfSessionsThatNeedToBeLoggedOut == 0) {
                logger.debug("No additional sessions that need to be logged out");
                return Collections.emptyList();
            } else if (numberOfSessionsThatNeedToBeLoggedOut == 1) {
                logger.info("Logging out oldest session");
            } else {
                logger.infof("Logging out oldest %s sessions", numberOfSessionsThatNeedToBeLoggedOut);
            }
    
            List<UserSessionModel> userSessionsToBeRemoved = userSessions
                .stream()
                .sorted(Comparator.comparingInt(UserSessionModel::getLastSessionRefresh))
                .limit(numberOfSessionsThatNeedToBeLoggedOut)
                .toList();
    
            for (UserSessionModel userSession : userSessionsToBeRemoved) {
                AuthenticationManager.backchannelLogout(session, userSession, true);
                eventBuilder.clone()
                    .event(EventType.LOGOUT)
                    .user(userSession.getUser())
                    .session(userSession.getId())
                    .success();
            }
    
            return userSessionsToBeRemoved;
        }
}
