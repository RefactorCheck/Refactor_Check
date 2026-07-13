public class keycloak_0112 {

        public static void saveTokens(AccessTokenResponse tokens, String endpoint, String realm, String clientId, String signKey, Long sigExpiresAt, String secret,
                                      String grantTypeForAuthentication) {
            handler.saveMergeConfig(config -> {
                config.setServerUrl(endpoint);
                config.setRealm(realm);

                RealmConfigData realmConfig = config.ensureRealmConfigData(endpoint, realm);
                realmConfig.setToken(tokens.getToken());
                realmConfig.setRefreshToken(tokens.getRefreshToken());
                realmConfig.setSigningToken(signKey);
                realmConfig.setSecret(secret);
                long currentTime = System.currentTimeMillis();
                realmConfig.setExpiresAt(currentTime + tokens.getExpiresIn() * 1000);
                if (realmConfig.getRefreshToken() != null) {
                    realmConfig.setRefreshExpiresAt(tokens.getRefreshExpiresIn() == 0 ?
                            Long.MAX_VALUE : currentTime + tokens.getRefreshExpiresIn() * 1000);
                }
                realmConfig.setSigExpiresAt(sigExpiresAt);
                realmConfig.setClientId(clientId);
                realmConfig.setGrantTypeForAuthentication(grantTypeForAuthentication);
            });
        }
}
