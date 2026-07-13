public class keycloak_0037 {

        protected Response exchangeStoredToken(UriInfo uriInfo, EventBuilder event, ClientModel authorizedClient, UserSessionModel tokenUserSession, UserModel tokenSubject) {
            RealmModel realm = authorizedClient != null ? authorizedClient.getRealm() : session.getContext().getRealm();
            FederatedIdentityModel model = session.users().getFederatedIdentity(realm, tokenSubject, getConfig().getAlias());

            if (model == null || model.getToken() == null) {
                reportInvalidToken(event, "requested_issuer is not linked");
                return exchangeNotLinked(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }

            String accessToken = extractTokenFromResponse(model.getToken(), getAccessTokenResponseParameter());
            if (accessToken == null) {
                model.setToken(null);
                session.users().updateFederatedIdentity(realm, tokenSubject, model);
                reportInvalidToken(event, "requested_issuer token expired");
                return exchangeTokenExpired(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }

            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            tokenResponse.setToken(accessToken);
            return buildTokenResponse(uriInfo, event, authorizedClient, tokenUserSession, tokenResponse, OAuth2Constants.ACCESS_TOKEN_TYPE);
        }

        private void reportInvalidToken(EventBuilder event, String reason) {
            if (event != null) {
                event.detail(Details.REASON, reason);
                event.error(Errors.INVALID_TOKEN);
            }
        }
}
