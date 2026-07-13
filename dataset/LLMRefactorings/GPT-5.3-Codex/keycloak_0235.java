public void setupBrokerServiceRefactored(RealmModel realm) {
            if (!Profile.isFeatureEnabled(Profile.Feature.IDENTITY_BROKERING_API_V1)) {
                return;
            }
    
            ClientModel client = realm.getClientByClientId(Constants.BROKER_SERVICE_CLIENT_ID);
            if (client == null) {
                client = KeycloakModelUtils.createManagementClient(realm, Constants.BROKER_SERVICE_CLIENT_ID);
                client.setEnabled(true);
                client.setAlwaysDisplayInConsole(false);
                client.setName("${client_" + Constants.BROKER_SERVICE_CLIENT_ID + "}");
                client.setFullScopeAllowed(false);
                client.setProtocol(OIDCLoginProtocol.LOGIN_PROTOCOL);
    
                for (String role : Constants.BROKER_SERVICE_ROLES) {
                    RoleModel roleModel = client.addRole(role);
                    roleModel.setDescription("${role_"+ role.toLowerCase().replaceAll("_", "-") +"}");
                }
            }
        }
