public class keycloak_0037 {

        protected Response exchangeStoredToken(UriInfo uriInfo, EventBuilder event, ClientModel authorizedClient, UserSessionModel tokenUserSession, UserModel tokenSubject) {
            RealmModel realm = authorizedClient != null ? authorizedClient.getRealm() : session.getContext().getRealm();
            FederatedIdentityModel model = session.users().getFederatedIdentity(realm, tokenSubject, getConfig().getAlias());
    
            if (model == null || model.getToken() == null) {
                if (event != null) {
                    event.detail(Details.REASON, "requested_issuer is not linked");
                    event.error(Errors.INVALID_TOKEN);
                }
                return exchangeNotLinked(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
    
            String accessToken = extractTokenFromResponse(model.getToken(), getAccessTokenResponseParameter());
            if (accessToken == null) {
                model.setToken(null);
                session.users().updateFederatedIdentity(realm, tokenSubject, model);
                if (event != null) {
                    event.detail(Details.REASON, "requested_issuer token expired");
                    event.error(Errors.INVALID_TOKEN);
                }
                return exchangeTokenExpired(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
    
            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            tokenResponse.setToken(accessToken);
            return buildTokenResponse(uriInfo, event, authorizedClient, tokenUserSession, tokenResponse, OAuth2Constants.ACCESS_TOKEN_TYPE);
        }
}
