public class keycloak_0202 {

        private void executeOnAuthorizationRequest(
                OIDCResponseType parsedResponseType,
                AuthorizationEndpointRequest request,
                String redirectUri) throws ClientPolicyException {
            ClientModel client = session.getContext().getClient();
            String codeChallenge = request.getCodeChallenge();
            String codeChallengeMethod = request.getCodeChallengeMethod();
            String pkceCodeChallengeMethod = OIDCAdvancedConfigWrapper.fromClientModel(client).getPkceCodeChallengeMethod();
    
            // check whether code challenge method is specified
            if (codeChallengeMethod == null) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Missing parameter: code_challenge_method");
            }
    
            // check whether acceptable code challenge method is specified
            if (!isAcceptableCodeChallengeMethod(codeChallengeMethod)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: invalid code_challenge_method");
            }
    
            // check whether specified code challenge method is configured one in advance
            if (pkceCodeChallengeMethod != null && !codeChallengeMethod.equals(pkceCodeChallengeMethod)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: code challenge method is not matching the configured one");
            }
    
            // check whether code challenge is specified
            if (codeChallenge == null) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Missing parameter: code_challenge");
            }
    
            // check whether code challenge is formatted along with the PKCE specification
            if (!isValidPkceCodeChallenge(codeChallenge)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: code_challenge");
            }
    
        }
}
