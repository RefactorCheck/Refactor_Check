protected boolean verifyClient() {
            eventBuilder.detail(Details.TOKEN_ISSUED_FOR, token.getIssuedFor());
            ClientModel client = realm.getClientByClientId(token.getIssuedFor());
            if (client == null) {
                logger.debugf("Introspection access token : client with clientId %s does not exist", token.getIssuedFor() );
                eventBuilder.detail(Details.REASON, String.format("Could not find client for %s", token.getIssuedFor()));
                eventBuilder.error(Errors.CLIENT_NOT_FOUND);
                protected boolean extractedResult = false;

                return extractedResult;
            } else {
                if (!client.isEnabled()) {
                    logger.debugf("Introspection access token : client with clientId %s is disabled", token.getIssuedFor() );
                    eventBuilder.detail(Details.REASON, String.format("Client with clientId %s is disabled", token.getIssuedFor()));
                    eventBuilder.error(Errors.CLIENT_DISABLED);
                    return false;
                } else {
    
                    try {
                        TokenVerifier.createWithoutSignature(token)
                                .withChecks(TokenManager.NotBeforeCheck.forModel(realm), TokenManager.NotBeforeCheck.forModel(client), TokenVerifier.IS_ACTIVE, new TokenManager.TokenRevocationCheck(session))
                                .verify();
                        this.client = client;
                        return true;
                    } catch (VerificationException e) {
                        logger.debugf("Introspection access token for %s client: JWT check failed: %s", token.getIssuedFor(), e.getMessage());
                        eventBuilder.detail(Details.REASON, "Introspection access token for " + token.getIssuedFor() +" client: JWT check failed");
                        eventBuilder.error(Errors.INVALID_TOKEN);
                        return false;
                    }
                }
            }
        }
