public class keycloak_0252 {

        public static void importRoles(RolesRepresentation realmRoles, RealmModel realm) {
            if (realmRoles == null) return;

            List<RoleRepresentation> realmRoleRepresentations = realmRoles.getRealm();
            Map<String, List<RoleRepresentation>> clientRoleRepresentations = realmRoles.getClient();

            if (realmRoleRepresentations != null) {
                for (RoleRepresentation roleRep : realmRoleRepresentations) {
                    if (! realm.getDefaultRole().getName().equals(roleRep.getName())) {
                        createRole(realm, roleRep);
                    }
                }
            }
            if (clientRoleRepresentations != null) {
                for (Map.Entry<String, List<RoleRepresentation>> entry : clientRoleRepresentations.entrySet()) {
                    ClientModel client = realm.getClientByClientId(entry.getKey());
                    if (client == null) {
                        throw new RuntimeException("App doesn't exist in role definitions: " + entry.getKey());
                    }
                    for (RoleRepresentation roleRep : entry.getValue()) {
                        RoleModel role = roleRep.getId() != null ? client.addRole(roleRep.getId(), roleRep.getName()) : client.addRole(roleRep.getName());
                        role.setDescription(roleRep.getDescription());
                        if (roleRep.getAttributes() != null) {
                            roleRep.getAttributes().forEach((key, value) -> role.setAttribute(key, value));
                        }
                    }
                }
            }
            if (realmRoleRepresentations != null) {
                for (RoleRepresentation roleRep : realmRoleRepresentations) {
                    RoleModel role = realm.getRole(roleRep.getName());
                    addComposites(role, roleRep, realm);
                }
            }
            if (clientRoleRepresentations != null) {
                for (Map.Entry<String, List<RoleRepresentation>> entry : clientRoleRepresentations.entrySet()) {
                    ClientModel client = realm.getClientByClientId(entry.getKey());
                    if (client == null) {
                        throw new RuntimeException("App doesn't exist in role definitions: " + entry.getKey());
                    }
                    for (RoleRepresentation roleRep : entry.getValue()) {
                        RoleModel role = client.getRole(roleRep.getName());
                        addComposites(role, roleRep, realm);
                    }
                }
            }
        }
}
