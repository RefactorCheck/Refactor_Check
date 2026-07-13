public class keycloak_0095 {

        @Override
        protected void validateAudience(AccessToken token, boolean disallowOnHolderOfTokenMismatch, List<ClientModel> targetAudienceClients) {
            ClientModel tokenHolder = token == null ? null : realm.getClientByClientId(token.getIssuedFor());
    
            if (client.isPublicClient()) {
                String errorMessage = "Public client is not allowed to exchange token";
                event.detail(Details.REASON, errorMessage);
                event.error(Errors.INVALID_CLIENT);
                throw new CorsErrorResponseException(cors, OAuthErrorException.INVALID_CLIENT, errorMessage, Response.Status.BAD_REQUEST);
            }
    
            for (ClientModel targetClient : targetAudienceClients) {
                if (!targetClient.isEnabled()) {
                    event.detail(Details.REASON, "audience client disabled");
                    event.detail(Details.AUDIENCE, targetClient.getClientId());
                    event.error(Errors.CLIENT_DISABLED);
                    throw new CorsErrorResponseException(cors, OAuthErrorException.INVALID_CLIENT, "Client disabled", Response.Status.BAD_REQUEST);
                }
            }
    
            //reject if the requester-client is not in the audience of the subject token
            if (!client.equals(tokenHolder)) {
                forbiddenIfClientIsNotWithinTokenAudience(token);
            }
        }
}
