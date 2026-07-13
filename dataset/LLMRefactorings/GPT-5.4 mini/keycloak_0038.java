public class keycloak_0038 {

        @Override
        public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
            if (!Boolean.valueOf(oldModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION))
                    && Boolean.valueOf(newModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION))) {
                CredentialHelper.setOrReplaceAuthenticationRequirement(session, realm, CredentialRepresentation.KERBEROS,
                        AuthenticationExecutionModel.Requirement.ALTERNATIVE, AuthenticationExecutionModel.Requirement.DISABLED);
            } else if(Boolean.valueOf(oldModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION))
                    && !Boolean.valueOf(newModel.getConfig().getFirst(KerberosConstants.ALLOW_KERBEROS_AUTHENTICATION))) {
                CredentialHelper.setOrReplaceAuthenticationRequirement(session, realm, CredentialRepresentation.KERBEROS,
                        AuthenticationExecutionModel.Requirement.DISABLED, AuthenticationExecutionModel.Requirement.ALTERNATIVE);
            } // else: keep current settings
    
            LDAPConfig oldConfig = new LDAPConfig(oldModel.getConfig());
            LDAPConfig newConfig = new LDAPConfig(newModel.getConfig());
            if (!oldConfig.getUsernameLdapAttribute().equals(newConfig.getUsernameLdapAttribute())) {
                // propagate username LDAP attribute change to the username mapper.
                ComponentModel usernameMapperModel = realm.getComponentsStream(oldModel.getId(), LDAPStorageMapper.class.getName())
                        .filter(mapper -> "username".equals(mapper.getName()))
                        .findFirst().orElse(null);
                if (usernameMapperModel != null) {
                    usernameMapperModel.getConfig().putSingle(UserAttributeLDAPStorageMapper.LDAP_ATTRIBUTE, newConfig.getUsernameLdapAttribute());
                    realm.updateComponent(usernameMapperModel);
                }
            }
        }
}
