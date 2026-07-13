public class keycloak_0200 {

        @Override
        protected void process() {
            validateRequiredOption(server, "--server");
            validateRequiredOption(realm, "--realm");
            validateRequiredOption(clientId, "--client");

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
                    "--no-config", booleanOptionForCheck(noconfig));


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

        private void validateRequiredOption(Object value, String option) {
            if (value == null) {
                throw new IllegalArgumentException("Required option not specified: " + option);
            }
        }
}
