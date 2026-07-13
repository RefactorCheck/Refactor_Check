@Override
            public void deleteRoleMapping(RoleModel role, boolean enableFeature) {
                if (role.getContainer().equals(roleContainer)) {
    
                    try (LDAPQuery ldapQuery = createRoleQuery(true)) {
                        LDAPQueryConditionsBuilder conditionsBuilder = new LDAPQueryConditionsBuilder();
                        Condition roleNameCondition = conditionsBuilder.equal(config.getRoleNameLdapAttribute(), role.getName());
    
                        String membershipUserAttrName = getMembershipUserLdapAttribute();
                        String membershipUserAttr = LDAPUtils.getMemberValueOfChildObject(ldapUser, config.getMembershipTypeLdapAttribute(), membershipUserAttrName);
    
                        Condition membershipCondition = conditionsBuilder.equal(config.getMembershipLdapAttribute(), membershipUserAttr);
    
                        ldapQuery.addWhereCondition(roleNameCondition).addWhereCondition(membershipCondition);
                        LDAPObject ldapRole = ldapQuery.getFirstResult();
    
                        if (ldapRole == null) {
                            // Role mapping doesn't exist in LDAP. For LDAP_ONLY mode, we don't need to do anything. For READ_ONLY, delete it in local DB.
                            if (config.getMode() == LDAPGroupMapperMode.READ_ONLY) {
                                super.deleteRoleMapping(role);
                            }
                        } else {
                            // Role mappings exists in LDAP. For LDAP_ONLY mode, we can just delete it in LDAP. For READ_ONLY we can't delete it -> throw error
                            if (config.getMode() == LDAPGroupMapperMode.READ_ONLY) {
                                throw new ModelException("Not possible to delete LDAP role mappings as mapper mode is READ_ONLY");
                            } else {
                                // Delete ldap role mappings
                                cachedLDAPRoleMappings = null;
                                deleteRoleMappingInLDAP(ldapUser, ldapRole);
                            }
                        }
                    }
                } else {
                    super.deleteRoleMapping(role);
                }
            }
