public class keycloak_0069 {

        public static UserConsentModel toModel(RealmModel newRealm, UserConsentRepresentation consentRep, KeycloakSession session) {
            ClientModel client = newRealm.getClientByClientId(consentRep.getClientId());
            if (client == null) {
                throw new RuntimeException("Unable to find client consent mappings for client: " + consentRep.getClientId());
            }
    
            UserConsentModel consentModel = new UserConsentModel(client);
            consentModel.setCreatedDate(consentRep.getCreatedDate());
            consentModel.setLastUpdatedDate(consentRep.getLastUpdatedDate());
    
            if (consentRep.getGrantedClientScopes() != null) {
                for (String scopeName : consentRep.getGrantedClientScopes()) {
                    ClientScopeModel clientScope = KeycloakModelUtils.getClientScopeByName(newRealm, scopeName);
                    if (clientScope != null) {
                        consentModel.addGrantedClientScope(clientScope);
                    } else if (Profile.isFeatureEnabled(Feature.PARAMETERIZED_SCOPES)) {
                        // check for parameterized scopes
                        AuthorizationRequestParserProvider clientScopeParser = session.getProvider(
                                AuthorizationRequestParserProvider.class, "client-scope");
                        if (clientScopeParser == null) {
                            throw new RuntimeException("No provider found for authorization requests parser client-scope");
                        }
    
                        AuthorizationRequestContext ctx = clientScopeParser.parseScopes(client, scopeName);
                        AuthorizationDetails authDetails = ctx.getAuthorizationDetailEntries().stream()
                                .filter(a -> a.getAuthorizationDetails().getParameterizedScopeParamFromCustomData() != null)
                                .findAny().orElse(null);
                        if (authDetails == null) {
                            throw new RuntimeException("Unable to find client scope referenced in consent mappings of user. Client scope name: " + scopeName);
                        }
    
                        consentModel.addGrantedClientScope(authDetails.getClientScope(), authDetails.getAuthorizationDetails().getParameterizedScopeParamFromCustomData());
                    } else {
                        throw new RuntimeException("Unable to find client scope referenced in consent mappings of user. Client scope name: " + scopeName);
                    }
                }
            }
    
            // Backwards compatibility. If user had consent for "offline_access" role, we treat it as he has consent for "offline_access" client scope
            if (consentRep.getGrantedRealmRoles() != null) {
                if (consentRep.getGrantedRealmRoles().contains(OAuth2Constants.OFFLINE_ACCESS)) {
                    ClientScopeModel offlineScope = client.getClientScopes(false).get(OAuth2Constants.OFFLINE_ACCESS);
                    if (offlineScope == null) {
                        logger.warn("Unable to find offline_access scope referenced in grantedRoles of user");
                    }
                    consentModel.addGrantedClientScope(offlineScope);
                }
            }
    
            return consentModel;
        }
}
