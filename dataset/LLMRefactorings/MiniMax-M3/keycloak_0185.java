public class keycloak_0185 {

    private static final Map<String, String> SAML_URL_ATTRIBUTES = Map.of(
            SamlProtocol.SAML_ASSERTION_CONSUMER_URL_POST_ATTRIBUTE, "Assertion Consumer Service POST Binding URL",
            SamlProtocol.SAML_ASSERTION_CONSUMER_URL_REDIRECT_ATTRIBUTE, "Assertion Consumer Service Redirect Binding URL",
            SamlProtocol.SAML_ASSERTION_CONSUMER_URL_ARTIFACT_ATTRIBUTE, "Artifact Binding URL",
            SamlProtocol.SAML_SINGLE_LOGOUT_SERVICE_URL_POST_ATTRIBUTE, "Logout Service POST Binding URL",
            SamlProtocol.SAML_SINGLE_LOGOUT_SERVICE_URL_ARTIFACT_ATTRIBUTE, "Logout Service ARTIFACT Binding URL",
            SamlProtocol.SAML_SINGLE_LOGOUT_SERVICE_URL_REDIRECT_ATTRIBUTE, "Logout Service Redirect Binding URL",
            SamlProtocol.SAML_SINGLE_LOGOUT_SERVICE_URL_SOAP_ATTRIBUTE, "Logout Service SOAP Binding URL",
            SamlProtocol.SAML_ARTIFACT_RESOLUTION_SERVICE_URL_ATTRIBUTE, "Artifact Resolution Service"
    );

        private void confirmSecureUris(ClientModel client) throws ClientPolicyException {
            if (!SamlProtocol.LOGIN_PROTOCOL.equals(client.getProtocol())) {
                return;
            }
    
            confirmSecureUri(client.getRootUrl(), "Root URL");
            confirmSecureUri(client.getManagementUrl(), "Master SAML Processing URL");
            confirmSecureUri(client.getBaseUrl(), "Home URL");
    
            confirmRedirectUris(RedirectUtils.resolveValidRedirects(session, client.getRootUrl(), client.getRedirectUris()),
                    "Valid redirect URIs");
    
            if (client.getAttributes() != null) {
                for (Map.Entry<String, String> attr : SAML_URL_ATTRIBUTES.entrySet()) {
                    confirmSecureUri(client.getAttribute(attr.getKey()), attr.getValue());
                }
            }
        }
}
