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
    
            // now update the config

            saveMergeConfig(config -> {
                RealmConfigData rdata = config.getRealmConfigData(server, realm);
                if (delete) {
                    if (rdata != null) {
                        rdata.getClients().remove(clientId);
                    }
                } else {
                    config.ensureRealmConfigData(server, realm).getClients().put(clientId, (token));
                }
            });
        }
