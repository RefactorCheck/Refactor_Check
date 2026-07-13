public class keycloak_0252 {

        public static void importRoles(RolesRepresentation realmRoles, RealmModel realm) {
            if (realmRoles == null) return;
    
            if (realmRoles.getRealm() != null) { // realm roles
                for (RoleRepresentation roleRep : realmRoles.getRealm()) {
                    if (! realm.getDefaultRole().getName().equals(roleRep.getName())) { // default role was already imported
                        createRole(realm, roleRep);
                    }
                }
            }
            if (realmRoles.getClient() != null) {
                for (Map.Entry<String, List<RoleRepresentation>> entry : realmRoles.getClient().entrySet()) {
                    ClientModel client = getClientOrThrow(realm, entry.getKey());
                    for (RoleRepresentation roleRep : entry.getValue()) {
                        // Application role may already exists (for example if it is defaultRole)
                        RoleModel role = roleRep.getId() != null ? client.addRole(roleRep.getId(), roleRep.getName()) : client.addRole(roleRep.getName());
                        role.setDescription(roleRep.getDescription());
                        if (roleRep.getAttributes() != null) {
                            roleRep.getAttributes().forEach((key, value) -> role.setAttribute(key, value));
                        }
                    }
                }
            }
            // now that all roles are created, re-iterate and set up composites
            if (realmRoles.getRealm() != null) { // realm roles
                for (RoleRepresentation roleRep : realmRoles.getRealm()) {
                    RoleModel role = realm.getRole(roleRep.getName());
                    addComposites(role, roleRep, realm);
                }
            }
            if (realmRoles.getClient() != null) {
                for (Map.Entry<String, List<RoleRepresentation>> entry : realmRoles.getClient().entrySet()) {
                    ClientModel client = getClientOrThrow(realm, entry.getKey());
                    for (RoleRepresentation roleRep : entry.getValue()) {
                        RoleModel role = client.getRole(roleRep.getName());
                        addComposites(role, roleRep, realm);
                    }
                }
            }
        }

        private static ClientModel getClientOrThrow(RealmModel realm, String clientId) {
            ClientModel client = realm.getClientByClientId(clientId);
            if (client == null) {
                throw new RuntimeException("App doesn't exist in role definitions: " + clientId);
            }
            return client;
        }
}
