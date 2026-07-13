public class keycloak_0200 {

        @Override
        protected void process() {
            if (server == null) {
                throw new IllegalArgumentException("Required option not specified: --server");
            }

            if (realm == null) {
                throw new IllegalArgumentException("Required option not specified: --realm");
            }

            if (clientId == null) {
                throw new IllegalArgumentException("Required option not specified: --client");
            }

            boolean noConfig = booleanOptionForCheck(noconfig);
            checkUnsupportedOptions(
                    "--user", user,
                    "--password", password,
                    "--secret", secret,
                    "--keystore", keystore,
                    "--storepass", storePass,
                    "--keypass", keyPass,
                    "--alias", alias,
                    "--truststore", trustStore,
                    "--trustpass", keyPass,
                    "--no-config", noConfig);


            if (!delete && token == null) {
                token = IoUtils.readPasswordFromConsole("Registration Access Token");
            }

            String registrationToken = token;
            saveMergeConfig(config -> {
                RealmConfigData rdata = config.getRealmConfigData(server, realm);
                if (delete) {
                    if (rdata != null) {
                        rdata.getClients().remove(clientId);
                    }
                } else {
                    config.ensureRealmConfigData(server, realm).getClients().put(clientId, registrationToken);
                }
            });
        }
}
