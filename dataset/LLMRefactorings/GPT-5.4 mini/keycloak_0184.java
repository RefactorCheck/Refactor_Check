public class keycloak_0184 {

            private RoleContainerModel resolveRoleContainer(KeycloakSession session, String roleContainer) {
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
                    String clientId = parts[1];
                    ClientModel client = session.clients().getClientByClientId(realm, clientId);
                    if (client == null) {
                        log.errorf("Unknown client: %s", clientId);
                        throw new HandledException();
                    }

                    return client;
                }
            }
}
