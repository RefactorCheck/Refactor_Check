public class keycloak_0279 {

        @Override
        public void onImportUserFromLDAP(LDAPObject ldapUser, UserModel user, RealmModel realm, boolean isCreate) {
            LDAPGroupMapperMode mode = config.getMode();
    
            // For now, import LDAP role mappings just during create
            if (mode == LDAPGroupMapperMode.IMPORT && isCreate) {
                String roleNameAttr = config.getRoleNameLdapAttribute();
                RoleContainerModel roleContainer = getTargetRoleContainer(realm);
                if (roleContainer == null) {
                    logger.warnf("Ignored client role grant for federation mapper '%s' as client not found: '%s'", mapperModel.getName(), config.getClientId());
                    return;
                }
                importLDAPRoleMappings(ldapUser, user, roleContainer, roleNameAttr);
            }
        }

        private void importLDAPRoleMappings(LDAPObject ldapUser, UserModel user, RoleContainerModel roleContainer, String roleNameAttr) {
            List<LDAPObject> ldapRoles = getLDAPRoleMappings(ldapUser);
            for (LDAPObject ldapRole : ldapRoles) {
                String roleName = ldapRole.getAttributeAsString(roleNameAttr);
                RoleModel role = roleContainer.getRole(roleName);
                if (role == null) {
                    role = roleContainer.addRole(roleName);
                }
                logger.debugf("Granting role [%s] to user [%s] during import from LDAP", roleName, user.getUsername());
                user.grantRole(role);
            }
        }
}
