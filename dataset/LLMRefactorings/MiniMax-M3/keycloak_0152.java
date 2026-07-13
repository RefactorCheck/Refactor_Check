public class keycloak_0152 {

        public ClientScopeModel addServiceAccountClientScope(RealmModel newRealm) {
            ClientScopeModel serviceAccountScope = KeycloakModelUtils.getClientScopeByName(newRealm, ServiceAccountConstants.SERVICE_ACCOUNT_SCOPE);
            if (serviceAccountScope == null) {
                serviceAccountScope = newRealm.addClientScope(ServiceAccountConstants.SERVICE_ACCOUNT_SCOPE);
                serviceAccountScope.setDescription("Specific scope for a client enabled for service accounts");
                serviceAccountScope.setDisplayOnConsentScreen(false);
                serviceAccountScope.setIncludeInTokenScope(false);
                serviceAccountScope.setProtocol(getId());
                addServiceAccountProtocolMappers(serviceAccountScope);

                logger.debugf("Client scope '%s' created in the realm '%s'.", ServiceAccountConstants.SERVICE_ACCOUNT_SCOPE, newRealm.getName());
            } else {
                logger.debugf("Client scope '%s' already exists in realm '%s'. Skip creating it.", ServiceAccountConstants.SERVICE_ACCOUNT_SCOPE, newRealm.getName());
            }

            return serviceAccountScope;
        }

        private void addServiceAccountProtocolMappers(ClientScopeModel serviceAccountScope) {
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
        }
}
