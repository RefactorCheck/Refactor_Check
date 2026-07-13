public class keycloak_0205 {

        private KeycloakIdentity createIdentity(boolean checkProtectionScope) {
            KeycloakIdentity identity = new KeycloakIdentity(this.authorization.getKeycloakSession());
            ResourceServer resourceServer = getResourceServer(identity);
            KeycloakSession keycloakSession = authorization.getKeycloakSession();
            RealmModel realm = keycloakSession.getContext().getRealm();
            ClientModel client = realm.getClientById(resourceServer.getClientId());
    
            if (checkProtectionScope) {
                if (identity.isResourceServer()) {
                    // if the identity is the resource server itself, then we don't need to check for uma_protection scope
                    return identity;
                }
    
                if (!identity.hasClientRole(client.getClientId(), "uma_protection")) {
                    throw new ErrorResponseException(OAuthErrorException.INVALID_SCOPE, "Requires uma_protection scope.", Status.FORBIDDEN);
                }
            }
    
            return identity;
        }
}
