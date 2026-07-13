public class keycloak_0061 {

        public static void checkIsUserValid(KeycloakSession session, RealmModel realm, String userId, Consumer<UserModel> userSetter, EventBuilder event) throws VerificationException {
            UserModel user = userId == null ? null : session.users().getUserById(realm, userId);
    
            if (user == null) {
                throw new ExplainedVerificationException(Errors.USER_NOT_FOUND, Messages.INVALID_USER);
            }
    
            if (! user.isEnabled()) {
                throw new ExplainedVerificationException(Errors.USER_DISABLED, Messages.ACCOUNT_DISABLED);
            }
    
            AuthResult authResult = AuthenticationManager.authenticateIdentityCookie(session, realm, true);
    
            if (authResult != null) {
                verifyAuthenticatedUserMatches(session, event, user, authResult);
            }
    
            if (userSetter != null) {
                userSetter.accept(user);
            }
        }

        public static void verifyAuthenticatedUserMatches(KeycloakSession session, EventBuilder event, UserModel user, AuthResult authResult) {
            UserSessionModel userSession = authResult.session();
            if (!user.equals(userSession.getUser())) {
                // do not allow authenticated users performing actions that are bound to other user and fire an event
                // it might be an attempt to hijack a user account or perform actions on behalf of others
                // we don't support yet multiple accounts within a same browser session
                event.detail(Details.EXISTING_USER, userSession.getUser().getId());
                event.error(Errors.DIFFERENT_USER_AUTHENTICATED);
                AuthenticationSessionModel authSession = session.getContext().getAuthenticationSession();
                throw new ErrorPageException(session, authSession, Response.Status.BAD_REQUEST, Messages.DIFFERENT_USER_AUTHENTICATED, userSession.getUser().getUsername());
            }
        }
}
