public class keycloak_0112 {

        public static void saveTokens(AccessTokenResponse tokens, String endpoint, String realm, String clientId, String signKey, Long sigExpiresAt, String secret,
                                      String grantTypeForAuthentication) {
            handler.saveMergeConfig(config -> {
                config.setServerUrl(endpoint);
                config.setRealm(realm);
    
                long currentTimeMillis = System.currentTimeMillis();
                RealmConfigData realmConfig = config.ensureRealmConfigData(endpoint, realm);
                realmConfig.setToken(tokens.getToken());
                realmConfig.setRefreshToken(tokens.getRefreshToken());
                realmConfig.setSigningToken(signKey);
                realmConfig.setSecret(secret);
                realmConfig.setExpiresAt(currentTimeMillis + tokens.getExpiresIn() * 1000);
                if (realmConfig.getRefreshToken() != null) {
                    realmConfig.setRefreshExpiresAt(tokens.getRefreshExpiresIn() == 0 ?
                            Long.MAX_VALUE : currentTimeMillis + tokens.getRefreshExpiresIn() * 1000);
                }
                realmConfig.setSigExpiresAt(sigExpiresAt);
                realmConfig.setClientId(clientId);
                realmConfig.setGrantTypeForAuthentication(grantTypeForAuthentication);
            });
        }
}
