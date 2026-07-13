public class keycloak_0110 {

        public static AuthenticationSessionModel clone(KeycloakSession session, AuthenticationSessionModel authSession) {
            AuthenticationSessionModel clone = authSession.getParentSession().createAuthenticationSession(authSession.getClient());
    
            clone.setRedirectUri(authSession.getRedirectUri());
            clone.setProtocol(authSession.getProtocol());
    
            for (Map.Entry<String, String> clientNote : authSession.getClientNotes().entrySet()) {
                clone.setClientNote(clientNote.getKey(), clientNote.getValue());
            }
    
            clone.setAuthNote(FORKED_FROM, authSession.getTabId());
            if (authSession.getAuthNote(AuthenticationManager.END_AFTER_REQUIRED_ACTIONS) != null) {
                clone.setAuthNote(AuthenticationManager.END_AFTER_REQUIRED_ACTIONS, authSession.getAuthNote(AuthenticationManager.END_AFTER_REQUIRED_ACTIONS));
            }
    
            logger.debugf("Forked authSession %s from authSession %s . Client: %s, Root session: %s",
                    clone.getTabId(), authSession.getTabId(), authSession.getClient().getClientId(), authSession.getParentSession().getId());
    
            return clone;
        }
}
