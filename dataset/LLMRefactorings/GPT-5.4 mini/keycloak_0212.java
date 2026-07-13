public class keycloak_0212 {

        protected boolean verifyClient() {
            String issuedFor = token.getIssuedFor();
            eventBuilder.detail(Details.TOKEN_ISSUED_FOR, issuedFor);
            ClientModel client = realm.getClientByClientId(issuedFor);
            if (client == null) {
                logger.debugf("Introspection access token : client with clientId %s does not exist", issuedFor);
                eventBuilder.detail(Details.REASON, String.format("Could not find client for %s", issuedFor));
                eventBuilder.error(Errors.CLIENT_NOT_FOUND);
                return false;
            } else {
                if (!client.isEnabled()) {
                    logger.debugf("Introspection access token : client with clientId %s is disabled", issuedFor);
                    eventBuilder.detail(Details.REASON, String.format("Client with clientId %s is disabled", issuedFor));
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
                        logger.debugf("Introspection access token for %s client: JWT check failed: %s", issuedFor, e.getMessage());
                        eventBuilder.detail(Details.REASON, "Introspection access token for " + issuedFor +" client: JWT check failed");
                        eventBuilder.error(Errors.INVALID_TOKEN);
                        return false;
                    }
                }
            }
        }
}
