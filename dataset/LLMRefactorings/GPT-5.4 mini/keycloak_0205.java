public class keycloak_0205 {

        private KeycloakIdentity createIdentity(boolean checkProtectionScope) {
            KeycloakIdentity identity = new KeycloakIdentity(this.authorization.getKeycloakSession());
            ResourceServer resourceServer = getResourceServer(identity);
            KeycloakSession keycloakSession = authorization.getKeycloakSession();
            RealmModel realm = keycloakSession.getContext().getRealm();
            ClientModel client = realm.getClientById(resourceServer.getClientId());

            if (checkProtectionScope) {
                validateProtectionScope(identity, client);
            }

            return identity;
        }

        private void validateProtectionScope(KeycloakIdentity identity, ClientModel client) {
            if (identity.isResourceServer()) {
                return;
            }

            if (!identity.hasClientRole(client.getClientId(), "uma_protection")) {
                throw new ErrorResponseException(OAuthErrorException.INVALID_SCOPE, "Requires uma_protection scope.", Status.FORBIDDEN);
            }
        }
}
