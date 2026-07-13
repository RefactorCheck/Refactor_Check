public class keycloak_0137 {

    public ClientScopeModel addAcrClientScope(RealmModel newRealm) {
        if (Profile.isFeatureEnabled(Profile.Feature.STEP_UP_AUTHENTICATION)) {
            return createAcrClientScope(newRealm);
        } else {
            logger.debugf("Skip creating client scope '%s' in the realm '%s' due the step-up authentication feature is disabled.", ACR_SCOPE, newRealm.getName());
            return null;
        }
    }

    private ClientScopeModel createAcrClientScope(RealmModel newRealm) {
        ClientScopeModel acrScope = KeycloakModelUtils.getClientScopeByName(newRealm, ACR_SCOPE);
        if (acrScope == null) {
            acrScope = newRealm.addClientScope(ACR_SCOPE);
            acrScope.setDescription("OpenID Connect scope for add acr (authentication context class reference) to the token");
            acrScope.setDisplayOnConsentScreen(false);
            acrScope.setIncludeInTokenScope(false);
            acrScope.setProtocol(OIDCLoginProtocol.LOGIN_PROTOCOL);
            acrScope.addProtocolMapper(builtins.get(ACR));

            newRealm.addDefaultClientScope(acrScope, true);

            logger.debugf("Client scope '%s' created in the realm '%s'.", ACR_SCOPE, newRealm.getName());
        } else {
            logger.debugf("Client scope '%s' already exists in realm '%s'. Skip creating it.", ACR_SCOPE, newRealm.getName());
        }
        return acrScope;
    }
}
