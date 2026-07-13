public class keycloak_0196 {

        public static void validateParameterizedClientScope(KeycloakSession session, ClientScopeRepresentation clientScope) throws ErrorResponseException {
            if (clientScope.getAttributes() == null) {
                return;
            }
            boolean isParameterized = Boolean.parseBoolean(clientScope.getAttributes().get(ClientScopeModel.IS_PARAMETERIZED_SCOPE));
            String regexp = clientScope.getAttributes().get(ClientScopeModel.PARAMETERIZED_SCOPE_REGEXP);
            String parameterType = clientScope.getAttributes().get(ClientScopeModel.PARAMETERIZED_SCOPE_TYPE);
            Map<String, String> attributes = clientScope.getAttributes();

            if (isParameterized) {
                if (StringUtil.isNullOrEmpty(parameterType)) {
                    throw ErrorResponse.error("Parameterized scope must have a parameter type", Response.Status.BAD_REQUEST);
                }

                if (session.getProvider(ParameterizedScopeTypeProvider.class, parameterType) == null) {
                    throw ErrorResponse.error(String.format("Invalid parameter type '%s'", parameterType), Response.Status.BAD_REQUEST);
                }

                if (CustomRegexScopeType.TYPE.equals(parameterType) && StringUtil.isNullOrEmpty(regexp)) {
                    throw ErrorResponse.error("Custom parameterized scope type requires a regex pattern", Response.Status.BAD_REQUEST);
                }

                if (!StringUtil.isNullOrEmpty(regexp) && !RegexUtils.isValidRegex(regexp, RegexUtils.DEFAULT_MAX_LENGTH, false)) {
                    throw ErrorResponse.error(String.format("Invalid regex for the Parameterized Scope regexp %1s", regexp), Response.Status.BAD_REQUEST);
                }
            }
        }
}
