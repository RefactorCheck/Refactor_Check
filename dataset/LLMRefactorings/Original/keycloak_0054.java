public class keycloak_0054 {

        private static void convertDeprecatedDefaultRoles(RealmRepresentation rep, RealmModel newRealm) {
            if (rep.getDefaultRole() == null) {
    
                // Setup realm default roles
                if (rep.getDefaultRoles() != null) {
                    rep.getDefaultRoles().stream()
                            .map(String::trim)
                            .map(name -> getOrAddRealmRole(newRealm, name))
                            .forEach(role -> newRealm.getDefaultRole().addCompositeRole(role));
                }
    
                // Setup client default roles
                if (rep.getClients() != null) {
                    for (ClientRepresentation clientRep : rep.getClients()) {
                        if (clientRep.getDefaultRoles() != null) {
                            Arrays.stream(clientRep.getDefaultRoles())
                                    .map(String::trim)
                                    .map(name -> getOrAddClientRole(newRealm.getClientById(clientRep.getId()), name))
                                    .forEach(role -> newRealm.getDefaultRole().addCompositeRole(role));
                        }
                    }
                }
            }
        }
}
