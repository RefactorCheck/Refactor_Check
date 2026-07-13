public class keycloak_0250 {

        public void validateParameterizedScopeUpdate(ClientScopeRepresentation rep) {
            validateClientScopeName(rep.getName());
    
            if (rep.getAttributes() != null
                    && rep.getAttributes().getOrDefault(ClientScopeModel.IS_PARAMETERIZED_SCOPE, "false").equalsIgnoreCase("true")
                    && !clientScope.isParameterizedScope()) {
                checkClientDefaultScopeAssignment();
                checkRealmDefaultScopeAssignment();
            }
            validateParameterizedClientScope(session, rep);
        }

        private void checkClientDefaultScopeAssignment() {
            Optional<String> scopeModelOpt = realm.getClientsStream()
                    .flatMap(clientModel -> clientModel.getClientScopes(true).values().stream())
                    .map(ClientScopeModel::getId)
                    .filter(scopeId -> scopeId.equalsIgnoreCase(this.clientScope.getId()))
                    .findAny();
            if (scopeModelOpt.isPresent()) {
                throw ErrorResponse.error("This Client Scope can't be made parameterized as it's assigned to a Client as a Default Scope",
                        Response.Status.BAD_REQUEST);
            }
        }

        private void checkRealmDefaultScopeAssignment() {
            boolean isRealmDefault = realm.getDefaultClientScopesStream(true)
                    .map(ClientScopeModel::getId)
                    .anyMatch(scopeId -> scopeId.equalsIgnoreCase(this.clientScope.getId()));
            if (isRealmDefault) {
                throw ErrorResponse.error("This Client Scope can't be made parameterized as it's assigned as a Realm Default Scope",
                        Response.Status.BAD_REQUEST);
            }
        }
}
