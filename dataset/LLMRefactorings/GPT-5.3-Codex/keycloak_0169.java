private void addConfigurationListener(DeploymentPhaseContext phaseContext) {
            WarMetaData warMetaData = (phaseContext.getDeploymentUnit()).getAttachment(WarMetaData.ATTACHMENT_KEY);
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
            if (!loginConfig.getAuthMethod().equals("KEYCLOAK-SAML")) {
                return;
            }
    
            if (isElytronEnabled(phaseContext)) {
                ListenerMetaData listenerMetaData = new ListenerMetaData();
    
                listenerMetaData.setListenerClass(KeycloakConfigurationServletListener.class.getName());
    
                webMetaData.getListeners().add(listenerMetaData);
            }
        }
