public class keycloak_0284 {

        private void checkRealmAdminManagementRoles(RealmModel realm) {
            if (realm.getName().equals(Config.getAdminRealm())) { return; }

            String realmAdminClientId = getRealmAdminClientId(realm);
            ClientModel realmAdminClient = realm.getClientByClientId(realmAdminClientId);
            RoleModel adminRole = getOrCreateAdminRole(realmAdminClient);

            for (String r : AdminRoles.ALL_REALM_ROLES) {
                RoleModel found = realmAdminClient.getRole(r);
                if (found == null) {
                    addAndSetAdminRole(r, realmAdminClient, adminRole);
                }
            }
            addQueryCompositeRoles(realmAdminClient);
        }

        private RoleModel getOrCreateAdminRole(ClientModel realmAdminClient) {
            RoleModel adminRole = realmAdminClient.getRole(AdminRoles.REALM_ADMIN);
            if (adminRole == null) {
                adminRole = realmAdminClient.addRole(AdminRoles.REALM_ADMIN);
                adminRole.setDescription("${role_" + AdminRoles.REALM_ADMIN + "}");
            }
            return adminRole;
        }
}
