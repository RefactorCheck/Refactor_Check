public class keycloak_0055 {

        @Override
        protected void process() {
            if (clientId == null) {
                throw new IllegalArgumentException("CLIENT not specified");
            }
    
            if (clientId.startsWith("-")) {
                warnfErr(CmdStdinContext.CLIENT_OPTION_WARN, clientId);
            }
    
            String regType = "default";
    
            ConfigData config = loadConfig();
            config = copyWithServerInfo(config);
    
            if (externalToken == null) {
                // if registration access token is not set via -t, try use the one from configuration
                externalToken = getRegistrationToken(config.sessionRealmConfigData(), clientId);
            }
    
            setupTruststore(config);
    
            String auth = externalToken;
            if (auth == null) {
                config = ensureAuthInfo(config);
                config = copyWithServerInfo(config);
                if (credentialsAvailable(config)) {
                    auth = ensureToken(config);
                }
            }
    
            auth = auth != null ? "Bearer " + auth : null;
    
    
            final String server = config.getServerUrl();
            final String realm = config.getRealm();
    
            doDelete(server + "/realms/" + realm + "/clients-registrations/" + regType + "/" + urlencode(clientId), auth);
    
            saveMergeConfig(cfg -> {
                cfg.ensureRealmConfigData(server, realm).getClients().remove(clientId);
            });
        }
}
