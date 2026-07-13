public class keycloak_0109 {

    @Override
    public String getCredentialType(KeycloakSession session, AuthenticationSessionModel authenticationSession) {
        String actionCredentialId = authenticationSession.getClientNote(Constants.KC_ACTION_PARAMETER);
        if (actionCredentialId == null) {
            return null;
        }

        UserModel user = authenticationSession.getAuthenticatedUser();
        if (user == null) {
            return null;
        }

        CredentialModel storedCredential = user.credentialManager().getStoredCredentialById(actionCredentialId);
        if (storedCredential == null) {
            if (actionCredentialId.endsWith("-id")) {
                return actionCredentialId.substring(0, actionCredentialId.length() - 3);
            } else {
                return null;
            }
        } else {
            return storedCredential.getType();
        }
    }
}
