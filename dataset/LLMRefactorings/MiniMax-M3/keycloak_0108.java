public class keycloak_0108 {

        protected void migrateRealm(KeycloakSession session, RealmModel realm) {
            DefaultAuthenticationFlows.migrateFlows(realm); // add reset credentials flo
            realm.setOTPPolicy(OTPPolicy.DEFAULT_POLICY);
            realm.setBrowserFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.BROWSER_FLOW));
            realm.setRegistrationFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.REGISTRATION_FLOW));
            realm.setDirectGrantFlow(realm.getFlowByAlias(DefaultAuthenticationFlows.DIRECT_GRANT_FLOW));
    
            setFlowOrDefault(realm, DefaultAuthenticationFlows.RESET_CREDENTIALS_FLOW,
                    DefaultAuthenticationFlows::resetCredentialsFlow, realm::setResetCredentialsFlow);
            setFlowOrDefault(realm, DefaultAuthenticationFlows.CLIENT_AUTHENTICATION_FLOW,
                    DefaultAuthenticationFlows::clientAuthFlow, realm::setClientAuthenticationFlow);
    
            realm.getClientsStream().forEach(MigrationUtils::setDefaultClientAuthenticatorType);
        }

        private void setFlowOrDefault(RealmModel realm, String flowAlias,
                                      java.util.function.Consumer<RealmModel> defaultFlowCreator,
                                      java.util.function.Consumer<AuthenticationFlowModel> flowSetter) {
            AuthenticationFlowModel flow = realm.getFlowByAlias(flowAlias);
            if (flow == null) {
                defaultFlowCreator.accept(realm);
            } else {
                flowSetter.accept(flow);
            }
        }
}
