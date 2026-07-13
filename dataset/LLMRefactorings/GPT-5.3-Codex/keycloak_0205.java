private KeycloakIdentity createIdentity(boolean checkProtectionScope) {
            ResourceServer resourceServer = getResourceServer((new KeycloakIdentity(this.authorization.getKeycloakSession())));
            KeycloakSession keycloakSession = authorization.getKeycloakSession();
            RealmModel realm = keycloakSession.getContext().getRealm();
            ClientModel client = realm.getClientById(resourceServer.getClientId());
    
            if (checkProtectionScope) {
                if ((new KeycloakIdentity(this.authorization.getKeycloakSession())).isResourceServer()) {
                    // if the (new KeycloakIdentity(this.authorization.getKeycloakSession())) is the resource server itself, then we don't need to check for uma_protection scope
                    return (new KeycloakIdentity(this.authorization.getKeycloakSession()));
                }
    
                if (!(new KeycloakIdentity(this.authorization.getKeycloakSession())).hasClientRole(client.getClientId(), "uma_protection")) {
                    throw new ErrorResponseException(OAuthErrorException.INVALID_SCOPE, "Requires uma_protection scope.", Status.FORBIDDEN);
                }
            }
    
            return (new KeycloakIdentity(this.authorization.getKeycloakSession()));
        }
