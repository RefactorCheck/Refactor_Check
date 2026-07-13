public class keycloak_0047 {

        public static void renameRealm(RealmModel realm, String name) {
            if (name.equals(realm.getName())) {
                return;
            }
            if (StringUtil.isBlank(name)) {
                throw new ModelException("Realm name cannot be empty");
            }
    
            String oldName = realm.getName();
    
            ClientModel masterApp = realm.getMasterAdminClient();
            masterApp.setClientId(KeycloakModelUtils.getMasterRealmAdminManagementClientId(name));
            realm.setName(name);
    
            ClientModel adminClient = realm.getClientByClientId(Constants.ADMIN_CONSOLE_CLIENT_ID);
            if (adminClient != null) {
                if (adminClient.getBaseUrl() != null) {
                    adminClient.setBaseUrl(adminClient.getBaseUrl().replace("/admin/" + oldName + "/", "/admin/" + name + "/"));
                }
                Set<String> adminRedirectUris = new HashSet<>();
                for (String r : adminClient.getRedirectUris()) {
                    adminRedirectUris.add(replace(r, "/admin/" + oldName + "/", "/admin/" + name + "/"));
                }
                adminClient.setRedirectUris(adminRedirectUris);
            }
    
            ClientModel accountClient = realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID);
            if (accountClient != null) {
                if (accountClient.getBaseUrl() != null) {
                    accountClient.setBaseUrl(accountClient.getBaseUrl().replace("/realms/" + oldName + "/", "/realms/" + name + "/"));
                }
                Set<String> accountRedirectUris = new HashSet<>();
                for (String r : accountClient.getRedirectUris()) {
                    accountRedirectUris.add(replace(r, "/realms/" + oldName + "/", "/realms/" + name + "/"));
                }
                accountClient.setRedirectUris(accountRedirectUris);
            }
    
            ClientModel accountConsoleClient = realm.getClientByClientId(Constants.ACCOUNT_CONSOLE_CLIENT_ID);
            if (accountConsoleClient != null) {
                if (accountConsoleClient.getBaseUrl() != null) {
                    accountConsoleClient.setBaseUrl(accountConsoleClient.getBaseUrl().replace("/realms/" + oldName + "/", "/realms/" + name + "/"));
                }
                Set<String> accountConsoleRedirectUris = new HashSet<>();
                for (String r : accountConsoleClient.getRedirectUris()) {
                    accountConsoleRedirectUris.add(replace(r, "/realms/" + oldName + "/", "/realms/" + name + "/"));
                }
                accountConsoleClient.setRedirectUris(accountConsoleRedirectUris);
            }
        }
}
