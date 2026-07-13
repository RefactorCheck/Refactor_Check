public class keycloak_0222 {

        protected Response exchangeStoredToken(UriInfo uriInfo, ClientModel authorizedClient, UserSessionModel tokenUserSession, UserModel tokenSubject) {
            FederatedIdentityModel model = session.users().getFederatedIdentity(authorizedClient.getRealm(), tokenSubject, getConfig().getAlias());
            if (model == null || model.getToken() == null) {
                return exchangeNotLinked(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
            String accessToken = model.getToken();
            if (accessToken == null) {
                model.setToken(null);
                session.users().updateFederatedIdentity(authorizedClient.getRealm(), tokenSubject, model);
                return exchangeTokenExpired(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
            AccessTokenResponse tokenResponse = createAccessTokenResponse(accessToken, uriInfo, authorizedClient, tokenUserSession);
            return Response.ok(tokenResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
        }

        private AccessTokenResponse createAccessTokenResponse(String accessToken, UriInfo uriInfo, ClientModel authorizedClient, UserSessionModel tokenUserSession) {
            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            tokenResponse.setToken(accessToken);
            tokenResponse.setIdToken(null);
            tokenResponse.setRefreshToken(null);
            tokenResponse.setRefreshExpiresIn(0);
            tokenResponse.getOtherClaims().clear();
            tokenResponse.getOtherClaims().put(OAuth2Constants.ISSUED_TOKEN_TYPE, TWITTER_TOKEN_TYPE);
            tokenResponse.getOtherClaims().put(ACCOUNT_LINK_URL, getLinkingUrl(uriInfo, authorizedClient, tokenUserSession));
            return tokenResponse;
        }
}
