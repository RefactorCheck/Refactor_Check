public class keycloak_0038 {

        @Override
        public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
            boolean allowKerberosCfgOld = Boolean.valueOf(oldModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION));
            boolean allowKerberosCfgNew = Boolean.valueOf(newModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION));
            if (!allowKerberosCfgOld && allowKerberosCfgNew) {
                CredentialHelper.setOrReplaceAuthenticationRequirement(session, realm, CredentialRepresentation.KERBEROS,
                        AuthenticationExecutionModel.Requirement.ALTERNATIVE, AuthenticationExecutionModel.Requirement.DISABLED);
            } else if(allowKerberosCfgOld && !allowKerberosCfgNew) {
                CredentialHelper.setOrReplaceAuthenticationRequirement(session, realm, CredentialRepresentation.KERBEROS,
                        AuthenticationExecutionModel.Requirement.DISABLED, AuthenticationExecutionModel.Requirement.ALTERNATIVE);
            } // else: keep current settings
    
            LDAPConfig oldConfig = new LDAPConfig(oldModel.getConfig());
            LDAPConfig newConfig = new LDAPConfig(newModel.getConfig());
            if (!oldConfig.getUsernameLdapAttribute().equals(newConfig.getUsernameLdapAttribute())) {
                propagateUsernameLdapAttributeChange(session, realm, oldModel, newConfig.getUsernameLdapAttribute());
            }
        }

        private void propagateUsernameLdapAttributeChange(KeycloakSession session, RealmModel realm, ComponentModel oldModel, String newUsernameLdapAttribute) {
            ComponentModel usernameMapperModel = realm.getComponentsStream(oldModel.getId(), LDAPStorageMapper.class.getName())
                    .filter(mapper -> "username".equals(mapper.getName()))
                    .findFirst().orElse(null);
            if (usernameMapperModel != null) {
                usernameMapperModel.getConfig().putSingle(UserAttributeLDAPStorageMapper.LDAP_ATTRIBUTE, newUsernameLdapAttribute);
                realm.updateComponent(usernameMapperModel);
            }
        }
}
