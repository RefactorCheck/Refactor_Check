public class keycloak_0057 {

        @Override
        public boolean matchCondition(AuthenticationFlowContext context) {
            final Map<String, String> config = context.getAuthenticatorConfig() != null
                    ? context.getAuthenticatorConfig().getConfig()
                    : Collections.emptyMap();
            final Set<String> credentials = new HashSet<>(Arrays.asList(Constants.CFG_DELIMITER_PATTERN.split(
                    config.getOrDefault(ConditionalCredentialAuthenticatorFactory.CONF_CREDENTIALS, ""))));
            final boolean included = Boolean.parseBoolean(config.get(ConditionalCredentialAuthenticatorFactory.CONF_INCLUDED));
    
            List<String> authCredentials = AuthenticatorUtil.getAuthnCredentials(context.getAuthenticationSession());
            if (authCredentials.isEmpty()) {
                authCredentials = Collections.singletonList(ConditionalCredentialAuthenticatorFactory.NONE_CREDENTIAL);
            }
            if (logger.isTraceEnabled()) {
                logger.tracef("Checking if any authentication credential '%s' is %s in %s", authCredentials, included? "included" : "not included", credentials);
            }
    
            // remove all credentials that are not used in the authentication
            credentials.retainAll(authCredentials);
    
            return included
                    ? !credentials.isEmpty()
                    : credentials.isEmpty();
        }
}
