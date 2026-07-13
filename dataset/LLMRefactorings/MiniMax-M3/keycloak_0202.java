public class keycloak_0202 {

        private void executeOnAuthorizationRequest(
                OIDCResponseType parsedResponseType,
                AuthorizationEndpointRequest request,
                String redirectUri) throws ClientPolicyException {
            ClientModel client = session.getContext().getClient();
            String codeChallenge = request.getCodeChallenge();
            String codeChallengeMethod = request.getCodeChallengeMethod();
            String pkceCodeChallengeMethod = OIDCAdvancedConfigWrapper.fromClientModel(client).getPkceCodeChallengeMethod();
    
            throwIfInvalid(codeChallengeMethod == null, "Missing parameter: code_challenge_method");
            throwIfInvalid(!isAcceptableCodeChallengeMethod(codeChallengeMethod), "Invalid parameter: invalid code_challenge_method");
            throwIfInvalid(pkceCodeChallengeMethod != null && !codeChallengeMethod.equals(pkceCodeChallengeMethod),
                    "Invalid parameter: code challenge method is not matching the configured one");
            throwIfInvalid(codeChallenge == null, "Missing parameter: code_challenge");
            throwIfInvalid(!isValidPkceCodeChallenge(codeChallenge), "Invalid parameter: code_challenge");
        }

        private void throwIfInvalid(boolean condition, String message) throws ClientPolicyException {
            if (condition) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, message);
            }
        }
}
