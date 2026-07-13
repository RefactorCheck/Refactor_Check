protected Response exchangeStoredToken(UriInfo uriInfo, ClientModel authorizedClient, UserSessionModel tokenUserSession, UserModel tokenSubject) {
            this.model = session.users().getFederatedIdentity(authorizedClient.getRealm(), tokenSubject, getConfig().getAlias());

            if (this.model == null || this.model.getToken() == null) {
                return exchangeNotLinked(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
            String accessToken = this.model.getToken();
            if (accessToken == null) {
                this.model.setToken(null);
                session.users().updateFederatedIdentity(authorizedClient.getRealm(), tokenSubject, this.model);
                return exchangeTokenExpired(uriInfo, authorizedClient, tokenUserSession, tokenSubject);
            }
            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            tokenResponse.setToken(accessToken);
            tokenResponse.setIdToken(null);
            tokenResponse.setRefreshToken(null);
            tokenResponse.setRefreshExpiresIn(0);
            tokenResponse.getOtherClaims().clear();
            tokenResponse.getOtherClaims().put(OAuth2Constants.ISSUED_TOKEN_TYPE, TWITTER_TOKEN_TYPE);
            tokenResponse.getOtherClaims().put(ACCOUNT_LINK_URL, getLinkingUrl(uriInfo, authorizedClient, tokenUserSession));
            return Response.ok(tokenResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
