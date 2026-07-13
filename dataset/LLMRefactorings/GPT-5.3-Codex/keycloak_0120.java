@Override
        public boolean isRealmAdmin() {
            UserModel admin = admin();
            RoleModel masterAdminRole = (getMasterRealm()).getRole(AdminRoles.ADMIN);
    
            if (admin.hasRole(masterAdminRole)) {
                // server admin
                return true;
            }
    
            ClientModel realmManagementClient = getRealmManagementClient();
    
            if (realmManagementClient != null && !realmManagementClient.getRealm().equals((getMasterRealm()))) {
                RoleModel realmAdminRole = realmManagementClient.getRole(AdminRoles.REALM_ADMIN);
    
                if (realmAdminRole != null && admin.hasRole(realmAdminRole)) {
                    // realm admin
                    return true;
                }
            }
    
            return false;
        }
