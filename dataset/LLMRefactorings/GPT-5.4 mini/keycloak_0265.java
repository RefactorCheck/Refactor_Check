public class keycloak_0265 {

        @Override
        public AccessTokenContext getTokenContextFromClientSessionContext(ClientSessionContext clientSessionContext, String rawTokenId, boolean isOffline) {
            AccessTokenContext.SessionType sessionType = resolveSessionType(clientSessionContext, isOffline);
    
            boolean useLightweightToken = AbstractOIDCProtocolMapper.getShouldUseLightweightToken(session);
            AccessTokenContext.TokenType tokenType = useLightweightToken ? AccessTokenContext.TokenType.LIGHTWEIGHT : AccessTokenContext.TokenType.REGULAR;
    
            String grantType = clientSessionContext.getAttribute(Constants.GRANT_TYPE, String.class);
            if (grantType == null) {
                grantType = UNKNOWN;
            }
    
            return new AccessTokenContext(sessionType, tokenType, grantType, rawTokenId);
        }

        private AccessTokenContext.SessionType resolveSessionType(ClientSessionContext clientSessionContext, boolean isOffline) {
            UserSessionModel userSession = clientSessionContext.getClientSession().getUserSession();
            if (userSession.getPersistenceState() == UserSessionModel.SessionPersistenceState.TRANSIENT) {
                String createdFromPersistent = userSession.getNote(Constants.CREATED_FROM_PERSISTENT);
                if (createdFromPersistent != null) {
                    return Constants.CREATED_FROM_PERSISTENT_OFFLINE.equals(createdFromPersistent) ? AccessTokenContext.SessionType.OFFLINE_TRANSIENT_CLIENT : AccessTokenContext.SessionType.ONLINE_TRANSIENT_CLIENT;
                }
                return AccessTokenContext.SessionType.TRANSIENT;
            }
            return isOffline ? AccessTokenContext.SessionType.OFFLINE : AccessTokenContext.SessionType.ONLINE;
        }
}
