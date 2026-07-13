public class keycloak_0274 {

        public static RoleRepresentation exportRole(RoleModel role) {
            RoleRepresentation roleRep = ModelToRepresentation.toRepresentation(role);

            Set<RoleModel> compositeRoles = role.getCompositesStream().collect(Collectors.toSet());
            if (compositeRoles != null && compositeRoles.size() > 0) {
                Set<String> compositeRealmRoles = null;
                Map<String, List<String>> compositeClientRoles = null;

                for (RoleModel composite : compositeRoles) {
                    RoleContainerModel crContainer = composite.getContainer();
                    if (crContainer instanceof RealmModel) {

                        if (compositeRealmRoles == null) {
                            compositeRealmRoles = new HashSet<>();
                        }
                        compositeRealmRoles.add(composite.getName());
                    } else {
                        if (compositeClientRoles == null) {
                            compositeClientRoles = new HashMap<>();
                        }

                        ClientModel app = (ClientModel)crContainer;
                        String appName = app.getClientId();
                        List<String> currentAppComposites = compositeClientRoles.get(appName);
                        if (currentAppComposites == null) {
                            currentAppComposites = new ArrayList<>();
                            compositeClientRoles.put(appName, currentAppComposites);
                        }
                        currentAppComposites.add(composite.getName());
                    }
                }

                RoleRepresentation.Composites compRep = new RoleRepresentation.Composites();
                if (compositeRealmRoles != null) {
                    compRep.setRealm(compositeRealmRoles);
                }
                if (compositeClientRoles != null) {
                    compRep.setClient(compositeClientRoles);
                }

                roleRep.setComposites(compRep);
            }

            return roleRep;
        }
}
