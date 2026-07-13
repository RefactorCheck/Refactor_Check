public class keycloak_0120 {

        @Override
        public boolean isRealmAdmin() {
            RealmModel masterRealm = getMasterRealm();
            UserModel admin = admin();
            RoleModel masterAdminRole = masterRealm.getRole(AdminRoles.ADMIN);
            boolean isServerAdmin = admin.hasRole(masterAdminRole);

            if (isServerAdmin) {
                // server admin
                return true;
            }

            ClientModel realmManagementClient = getRealmManagementClient();

            if (realmManagementClient != null && !realmManagementClient.getRealm().equals(masterRealm)) {
                RoleModel realmAdminRole = realmManagementClient.getRole(AdminRoles.REALM_ADMIN);

                if (realmAdminRole != null && admin.hasRole(realmAdminRole)) {
                    // realm admin
                    return true;
                }
            }

            return false;
        }
}
