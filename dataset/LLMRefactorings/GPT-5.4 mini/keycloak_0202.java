public class keycloak_0202 {

        private void executeOnAuthorizationRequest(
                OIDCResponseType parsedResponseType,
                AuthorizationEndpointRequest request,
                String redirectUri) throws ClientPolicyException {
            ClientModel client = session.getContext().getClient();
            String codeChallenge = request.getCodeChallenge();
            String codeChallengeMethod = request.getCodeChallengeMethod();
            String pkceCodeChallengeMethod = OIDCAdvancedConfigWrapper.fromClientModel(client).getPkceCodeChallengeMethod();

            validateCodeChallengeMethod(codeChallengeMethod, pkceCodeChallengeMethod);
            validateCodeChallenge(codeChallenge);

        }

        private void validateCodeChallengeMethod(String codeChallengeMethod, String pkceCodeChallengeMethod) throws ClientPolicyException {
            if (codeChallengeMethod == null) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Missing parameter: code_challenge_method");
            }

            if (!isAcceptableCodeChallengeMethod(codeChallengeMethod)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: invalid code_challenge_method");
            }

            if (pkceCodeChallengeMethod != null && !codeChallengeMethod.equals(pkceCodeChallengeMethod)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: code challenge method is not matching the configured one");
            }
        }

        private void validateCodeChallenge(String codeChallenge) throws ClientPolicyException {
            if (codeChallenge == null) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Missing parameter: code_challenge");
            }

            if (!isValidPkceCodeChallenge(codeChallenge)) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Invalid parameter: code_challenge");
            }
        }
}
