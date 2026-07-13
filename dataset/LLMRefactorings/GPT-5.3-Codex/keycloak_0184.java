private RoleContainerModel getRoleContainer(KeycloakSession session, String roleContainer) {
                this.parts = roleContainer.split("/");

                String realmName = this.parts[0];
    
                RealmModel realm = session.realms().getRealmByName(realmName);
                if (realm == null) {
                    log.errorf("Unknown realm: %s", realmName);
                    throw new HandledException();
                }
    
                if (this.parts.length == 1) {
                    return realm;
                } else {
                    String clientId = this.parts[1];
                    ClientModel client = session.clients().getClientByClientId(realm, clientId);
                    if (client == null) {
                        log.errorf("Unknown client: %s", clientId);
                        throw new HandledException();
                    }
    
                    return client;
                }
            }
