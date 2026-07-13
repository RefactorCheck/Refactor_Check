public class keycloak_0055 {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DEFAULT_REG_TYPE = "default";
    private static final String CLIENT_NOT_SPECIFIED = "CLIENT not specified";

    @Override
    protected void process() {
        if (clientId == null) {
            throw new IllegalArgumentException(CLIENT_NOT_SPECIFIED);
        }

        if (clientId.startsWith("-")) {
            warnfErr(CmdStdinContext.CLIENT_OPTION_WARN, clientId);
        }

        String regType = DEFAULT_REG_TYPE;

        ConfigData config = loadConfig();
        config = copyWithServerInfo(config);

        if (externalToken == null) {
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

        auth = auth != null ? BEARER_PREFIX + auth : null;


        final String server = config.getServerUrl();
        final String realm = config.getRealm();

        doDelete(server + "/realms/" + realm + "/clients-registrations/" + regType + "/" + urlencode(clientId), auth);

        saveMergeConfig(cfg -> {
            cfg.ensureRealmConfigData(server, realm).getClients().remove(clientId);
        });
    }
}
