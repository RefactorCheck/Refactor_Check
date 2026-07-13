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

        updateClientUrls(realm.getClientByClientId(Constants.ADMIN_CONSOLE_CLIENT_ID), oldName, name, "admin");
        updateClientUrls(realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID), oldName, name, "realms");
        updateClientUrls(realm.getClientByClientId(Constants.ACCOUNT_CONSOLE_CLIENT_ID), oldName, name, "realms");
    }

    private static void updateClientUrls(ClientModel client, String oldName, String name, String pathSegment) {
        if (client == null) {
            return;
        }
        String oldPath = "/" + pathSegment + "/" + oldName + "/";
        String newPath = "/" + pathSegment + "/" + name + "/";
        if (client.getBaseUrl() != null) {
            client.setBaseUrl(client.getBaseUrl().replace(oldPath, newPath));
        }
        Set<String> redirectUris = new HashSet<>();
        for (String r : client.getRedirectUris()) {
            redirectUris.add(replace(r, oldPath, newPath));
        }
        client.setRedirectUris(redirectUris);
    }
}
