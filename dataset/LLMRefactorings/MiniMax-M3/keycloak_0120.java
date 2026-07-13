public class keycloak_0120 {

        @Override
        public boolean isRealmAdmin() {
            RealmModel masterRealm = getMasterRealm();
            UserModel admin = admin();
            RoleModel masterAdminRole = masterRealm.getRole(AdminRoles.ADMIN);
    
            if (admin.hasRole(masterAdminRole)) {
                return true;
            }
    
            return isRealmAdminForClient(admin, masterRealm);
        }

        private boolean isRealmAdminForClient(UserModel admin, RealmModel masterRealm) {
            ClientModel realmManagementClient = getRealmManagementClient();
    
            if (realmManagementClient != null && !realmManagementClient.getRealm().equals(masterRealm)) {
                RoleModel realmAdminRole = realmManagementClient.getRole(AdminRoles.REALM_ADMIN);
    
                if (realmAdminRole != null && admin.hasRole(realmAdminRole)) {
                    return true;
                }
            }
    
            return false;
        }
}
