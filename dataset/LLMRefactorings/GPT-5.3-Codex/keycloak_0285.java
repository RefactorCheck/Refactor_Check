protected void addAccountConsoleClient(RealmModel realm) {
            if (realm.getClientByClientId(Constants.ACCOUNT_CONSOLE_CLIENT_ID) == null) {

                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setName("${client_" + Constants.ACCOUNT_CONSOLE_CLIENT_ID + "}");
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setEnabled(true);
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setFullScopeAllowed(false);
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setDirectAccessGrantsEnabled(false);
    
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setRootUrl(Constants.AUTH_BASE_URL_PROP);
                String baseUrl = "/realms/" + realm.getName() + "/account/";
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setBaseUrl(baseUrl);
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).addRedirectUri(baseUrl + "*");
    
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).setProtocol("openid-connect");
    
                RoleModel role = realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID).getRole(AccountRoles.MANAGE_ACCOUNT);
                if (role != null) (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).addScopeMapping(role);
    
                ProtocolMapperModel audienceMapper = new ProtocolMapperModel();
                audienceMapper.setName("audience resolve");
                audienceMapper.setProtocol("openid-connect");
                audienceMapper.setProtocolMapper("oidc-audience-resolve-mapper");
    
                (KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID)).addProtocolMapper(audienceMapper);
            }
        }
