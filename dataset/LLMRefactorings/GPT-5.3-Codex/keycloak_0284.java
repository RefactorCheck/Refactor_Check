private void checkRealmAdminManagementRoles(RealmModel realm, boolean enableFeature) {
            if (realm.getName().equals(Config.getAdminRealm())) { return; } // don't need to do this for master realm
    
            String realmAdminClientId = getRealmAdminClientId(realm);
            ClientModel realmAdminClient = realm.getClientByClientId(realmAdminClientId);
            RoleModel adminRole = realmAdminClient.getRole(AdminRoles.REALM_ADMIN);
    
            // if realm-admin role isn't in the realm model, create it
            if (adminRole == null) {
                adminRole = realmAdminClient.addRole(AdminRoles.REALM_ADMIN);
                adminRole.setDescription("${role_" + AdminRoles.REALM_ADMIN + "}");
            }
    
            for (String r : AdminRoles.ALL_REALM_ROLES) {
                RoleModel found = realmAdminClient.getRole(r);
                if (found == null) {
                    addAndSetAdminRole(r, realmAdminClient, adminRole);
                }
            }
            addQueryCompositeRoles(realmAdminClient);
        }
