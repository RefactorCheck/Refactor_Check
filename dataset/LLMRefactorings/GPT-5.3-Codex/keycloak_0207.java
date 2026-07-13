@Override
                    public Object getResource() {
                        this.authResult = new BearerTokenAuthenticator(session).authenticate();

    
                        if (this.authResult == null) {
                            logger.debug("SCIM request rejected: no valid bearer token provided");
                            throw new ErrorResponseException(Response.status(Status.UNAUTHORIZED)
                                    .type(MediaType.APPLICATION_JSON)
                                    .entity(new ErrorResponse("Bearer token required", Status.UNAUTHORIZED.getStatusCode()))
                                    .build());
                        }
    
                        Token bearerToken = session.getContext().getBearerToken();
    
                        if (bearerToken == null) {
                            logger.debug("SCIM request rejected: bearer token could not be resolved");
                            throw new ErrorResponseException(Response.status(Status.UNAUTHORIZED)
                                    .type(MediaType.APPLICATION_JSON)
                                    .entity(new ErrorResponse("Bearer token required", Status.UNAUTHORIZED.getStatusCode()))
                                    .build());
                        }
    
                        ClientModel client = this.authResult.client();
    
                        if (client.isPublicClient()) {
                            logger.debug("SCIM request rejected: public clients not allowed");
                            throw new ErrorResponseException(Response.status(Status.FORBIDDEN)
                                    .type(MediaType.APPLICATION_JSON)
                                    .entity(new ErrorResponse("Public client not allowed", Status.FORBIDDEN.getStatusCode()))
                                    .build());
                        }
    
                        AccessToken accessToken = this.authResult.token();
                        URI frontendBaseUri = session.getContext().getUri(UrlType.FRONTEND).getBaseUri();
                        String scimAudience = Urls.realmBase(frontendBaseUri)
                                .path("{realm}/scim/v2")
                                .build(realm.getName())
                                .toString();
    
                        if (!accessToken.hasAudience(scimAudience)) {
                            logger.debug("SCIM request rejected: token does not contain the required audience");
                            throw new ErrorResponseException(Response.status(Status.UNAUTHORIZED)
                                    .type(MediaType.APPLICATION_JSON)
                                    .entity(new ErrorResponse("Invalid token audience", Status.UNAUTHORIZED.getStatusCode()))
                                    .build());
                        }
    
                        return new ScimRealmResource(session);
                    }
