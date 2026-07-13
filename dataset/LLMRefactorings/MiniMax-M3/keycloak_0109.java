public class keycloak_0109 {

        @Override
        public String getCredentialType(KeycloakSession session, AuthenticationSessionModel authenticationSession) {
            String credentialId = authenticationSession.getClientNote(Constants.KC_ACTION_PARAMETER);
            if (credentialId == null) {
                return null;
            }
    
            UserModel user = authenticationSession.getAuthenticatedUser();
            if (user == null) {
                return null;
            }
    
            CredentialModel credential = user.credentialManager().getStoredCredentialById(credentialId);
            if (credential == null) {
                return extractCredentialTypeFromId(credentialId);
            } else {
                return credential.getType();
            }
        }
    
        private String extractCredentialTypeFromId(String credentialId) {
            if (credentialId.endsWith("-id")) {
                return credentialId.substring(0, credentialId.length() - 3);
            } else {
                return null;
            }
        }
}
