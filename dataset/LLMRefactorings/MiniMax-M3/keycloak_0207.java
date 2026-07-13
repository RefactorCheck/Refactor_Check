public class keycloak_0207 {

                    @Override
                    public Object getResource() {
                        AuthResult authResult = new BearerTokenAuthenticator(session).authenticate();
    
                        if (authResult == null) {
                            logger.debug("SCIM request rejected: no valid bearer token provided");
                            throw buildUnauthorized("Bearer token required");
                        }
    
                        Token bearerToken = session.getContext().getBearerToken();
    
                        if (bearerToken == null) {
                            logger.debug("SCIM request rejected: bearer token could not be resolved");
                            throw buildUnauthorized("Bearer token required");
                        }
    
                        ClientModel client = authResult.client();
    
                        if (client.isPublicClient()) {
                            logger.debug("SCIM request rejected: public clients not allowed");
                            throw buildForbidden("Public client not allowed");
                        }
    
                        AccessToken accessToken = authResult.token();
                        URI frontendBaseUri = session.getContext().getUri(UrlType.FRONTEND).getBaseUri();
                        String scimAudience = Urls.realmBase(frontendBaseUri)
                                .path("{realm}/scim/v2")
                                .build(realm.getName())
                                .toString();
    
                        if (!accessToken.hasAudience(scimAudience)) {
                            logger.debug("SCIM request rejected: token does not contain the required audience");
                            throw buildUnauthorized("Invalid token audience");
                        }
    
                        return new ScimRealmResource(session);
                    }

                    private ErrorResponseException buildUnauthorized(String message) {
                        return new ErrorResponseException(Response.status(Status.UNAUTHORIZED)
                                .type(MediaType.APPLICATION_JSON)
                                .entity(new ErrorResponse(message, Status.UNAUTHORIZED.getStatusCode()))
                                .build());
                    }

                    private ErrorResponseException buildForbidden(String message) {
                        return new ErrorResponseException(Response.status(Status.FORBIDDEN)
                                .type(MediaType.APPLICATION_JSON)
                                .entity(new ErrorResponse(message, Status.FORBIDDEN.getStatusCode()))
                                .build());
                    }
}
