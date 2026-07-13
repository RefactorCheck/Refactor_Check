public class keycloak_0108 {

    private static final String RESET_CREDENTIALS_FLOW_ALIAS = DefaultAuthenticationFlows.RESET_CREDENTIALS_FLOW;
    private static final String CLIENT_AUTHENTICATION_FLOW_ALIAS = DefaultAuthenticationFlows.CLIENT_AUTHENTICATION_FLOW;

    protected void migrateRealm(KeycloakSession session, RealmModel realm) {
        DefaultAuthenticationFlows.migrateFlows(realm); // add reset credentials flo
        realm.setOTPPolicy(OTPPolicy.DEFAULT_POLICY);
        realm.setBrowserFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.BROWSER_FLOW));
        realm.setRegistrationFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.REGISTRATION_FLOW));
        realm.setDirectGrantFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.DIRECT_GRANT_FLOW));

        AuthenticationFlowModel resetFlow = realm.getFlowByAlias(RESET_CREDENTIALS_FLOW_ALIAS);
        if (resetFlow == null) {
            DefaultAuthenticationFlows.resetCredentialsFlow(realm);
        } else {
            realm.setResetCredentialsFlow(resetFlow);
        }

        AuthenticationFlowModel clientAuthFlow = realm.getFlowByAlias(CLIENT_AUTHENTICATION_FLOW_ALIAS);
        if (clientAuthFlow == null) {
            DefaultAuthenticationFlows.clientAuthFlow(realm);
        } else {
            realm.setClientAuthenticationFlow(clientAuthFlow);
        }

        realm.getClientsStream().forEach(MigrationUtils::setDefaultClientAuthenticatorType);
    }
}
