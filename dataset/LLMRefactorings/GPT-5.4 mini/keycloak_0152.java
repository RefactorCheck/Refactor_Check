public class keycloak_0152 {

        public ClientScopeModel addServiceAccountClientScope(RealmModel newRealm) {
            String scopeName = ServiceAccountConstants.SERVICE_ACCOUNT_SCOPE;
            ClientScopeModel serviceAccountScope = KeycloakModelUtils.getClientScopeByName(newRealm, scopeName);
            if (serviceAccountScope == null) {
                serviceAccountScope = newRealm.addClientScope(scopeName);
                serviceAccountScope.setDescription("Specific scope for a client enabled for service accounts");
                serviceAccountScope.setDisplayOnConsentScreen(false);
                serviceAccountScope.setIncludeInTokenScope(false);
                serviceAccountScope.setProtocol(getId());
                serviceAccountScope.addProtocolMapper(UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_ID_PROTOCOL_MAPPER,
                        ServiceAccountConstants.CLIENT_ID,
                        ServiceAccountConstants.CLIENT_ID, "String",
                        true, true, true));
                serviceAccountScope.addProtocolMapper(UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_HOST_PROTOCOL_MAPPER,
                        ServiceAccountConstants.CLIENT_HOST,
                        ServiceAccountConstants.CLIENT_HOST, "String",
                        true, true, true));
                serviceAccountScope.addProtocolMapper(UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_ADDRESS_PROTOCOL_MAPPER,
                        ServiceAccountConstants.CLIENT_ADDRESS,
                        ServiceAccountConstants.CLIENT_ADDRESS, "String",
                        true, true, true));
    
                logger.debugf("Client scope '%s' created in the realm '%s'.", scopeName, newRealm.getName());
            } else {
                logger.debugf("Client scope '%s' already exists in realm '%s'. Skip creating it.", scopeName, newRealm.getName());
            }
    
            return serviceAccountScope;
        }
}
