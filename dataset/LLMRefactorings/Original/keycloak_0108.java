public class keycloak_0108 {

        protected void migrateRealm(KeycloakSession session, RealmModel realm) {
            DefaultAuthenticationFlows.migrateFlows(realm); // add reset credentials flo
            realm.setOTPPolicy(OTPPolicy.DEFAULT_POLICY);
            realm.setBrowserFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.BROWSER_FLOW));
            realm.setRegistrationFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.REGISTRATION_FLOW));
            realm.setDirectGrantFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.DIRECT_GRANT_FLOW));
    
            AuthenticationFlowModel resetFlow = realm.getFlowByAlias(DefaultAuthenticationFlows.RESET_CREDENTIALS_FLOW);
            if (resetFlow == null) {
                DefaultAuthenticationFlows.resetCredentialsFlow(realm);
            } else {
                realm.setResetCredentialsFlow(resetFlow);
            }
    
            AuthenticationFlowModel clientAuthFlow = realm.getFlowByAlias(DefaultAuthenticationFlows.CLIENT_AUTHENTICATION_FLOW);
            if (clientAuthFlow == null) {
                DefaultAuthenticationFlows.clientAuthFlow(realm);
            } else {
                realm.setClientAuthenticationFlow(clientAuthFlow);
            }
    
            realm.getClientsStream().forEach(MigrationUtils::setDefaultClientAuthenticatorType);
        }
}
