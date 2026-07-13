public class keycloak_0169 {

        private static final String KEYCLOAK_SAML = "KEYCLOAK-SAML";

        private void addConfigurationListener(DeploymentPhaseContext phaseContext) {
            DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
            WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
            if (warMetaData == null) {
                return;
            }

            JBossWebMetaData webMetaData = warMetaData.getMergedJBossWebMetaData();
            if (webMetaData == null) {
                webMetaData = new JBossWebMetaData();
                warMetaData.setMergedJBossWebMetaData(webMetaData);
            }

            LoginConfigMetaData loginConfig = webMetaData.getLoginConfig();
            if (loginConfig == null) {
                return;
            }
            if (!loginConfig.getAuthMethod().equals(KEYCLOAK_SAML)) {
                return;
            }

            if (isElytronEnabled(phaseContext)) {
                ListenerMetaData listenerMetaData = new ListenerMetaData();

                listenerMetaData.setListenerClass(KeycloakConfigurationServletListener.class.getName());

                webMetaData.getListeners().add(listenerMetaData);
            }
        }
}
