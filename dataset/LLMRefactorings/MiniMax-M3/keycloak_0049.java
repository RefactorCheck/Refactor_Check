public class keycloak_0049 {

    @Override
    public void deleteRoleMapping(RoleModel role) {
        if (role.getContainer().equals(roleContainer)) {
            deleteLDAPRoleMapping(role);
        } else {
            super.deleteRoleMapping(role);
        }
    }

    private void deleteLDAPRoleMapping(RoleModel role) {
        try (LDAPQuery ldapQuery = createRoleQuery(true)) {
            LDAPQueryConditionsBuilder conditionsBuilder = new LDAPQueryConditionsBuilder();
            Condition roleNameCondition = conditionsBuilder.equal(config.getRoleNameLdapAttribute(), role.getName());

            String membershipUserAttrName = getMembershipUserLdapAttribute();
            String membershipUserAttr = LDAPUtils.getMemberValueOfChildObject(ldapUser, config.getMembershipTypeLdapAttribute(), membershipUserAttrName);

            Condition membershipCondition = conditionsBuilder.equal(config.getMembershipLdapAttribute(), membershipUserAttr);

            ldapQuery.addWhereCondition(roleNameCondition).addWhereCondition(membershipCondition);
            LDAPObject ldapRole = ldapQuery.getFirstResult();

            if (ldapRole == null) {
                if (config.getMode() == LDAPGroupMapperMode.READ_ONLY) {
                    super.deleteRoleMapping(role);
                }
            } else {
                if (config.getMode() == LDAPGroupMapperMode.READ_ONLY) {
                    throw new ModelException("Not possible to delete LDAP role mappings as mapper mode is READ_ONLY");
                } else {
                    cachedLDAPRoleMappings = null;
                    deleteRoleMappingInLDAP(ldapUser, ldapRole);
                }
            }
        }
    }
}
