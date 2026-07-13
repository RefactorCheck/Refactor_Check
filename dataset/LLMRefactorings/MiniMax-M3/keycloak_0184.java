public class keycloak_0184 {

            private RoleContainerModel getRoleContainer(KeycloakSession session, String roleContainer) {
                String[] parts = roleContainer.split("/");
                String realmName = parts[0];

                RealmModel realm = session.realms().getRealmByName(realmName);
                if (realm == null) {
                    log.errorf("Unknown realm: %s", realmName);
                    throw new HandledException();
                }

                if (parts.length == 1) {
                    return realm;
                } else {
                    return getClient(session, realm, parts[1]);
                }
            }

            private ClientModel getClient(KeycloakSession session, RealmModel realm, String clientId) {
                ClientModel client = session.clients().getClientByClientId(realm, clientId);
                if (client == null) {
                    log.errorf("Unknown client: %s", clientId);
                    throw new HandledException();
                }
                return client;
            }
}
