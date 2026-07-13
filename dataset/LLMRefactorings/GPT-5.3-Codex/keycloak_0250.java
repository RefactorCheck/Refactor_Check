public void validateParameterizedScopeUpdate(ClientScopeRepresentation rep, boolean enableFeature) {
            validateClientScopeName(rep.getName());
    
            // Only check this if the representation has been sent to make it dynamic
            if (rep.getAttributes() != null
                    && rep.getAttributes().getOrDefault(ClientScopeModel.IS_PARAMETERIZED_SCOPE, "false").equalsIgnoreCase("true")
                    && !clientScope.isParameterizedScope()) {
                Optional<String> scopeModelOpt = realm.getClientsStream()
                        .flatMap(clientModel -> clientModel.getClientScopes(true).values().stream())
                        .map(ClientScopeModel::getId)
                        .filter(scopeId -> scopeId.equalsIgnoreCase(this.clientScope.getId()))
                        .findAny();
                // if it's present, it means that a client has this scope assigned as a default scope, so this scope can't be made parameterized
                if (scopeModelOpt.isPresent()) {
                    throw ErrorResponse.error("This Client Scope can't be made parameterized as it's assigned to a Client as a Default Scope",
                            Response.Status.BAD_REQUEST);
                }
    
                // Also check realm-level default scopes — a scope that is a realm default would be automatically
                // assigned to new clients as a default scope, which is incompatible with parameterized scopes.
                boolean isRealmDefault = realm.getDefaultClientScopesStream(true)
                        .map(ClientScopeModel::getId)
                        .anyMatch(scopeId -> scopeId.equalsIgnoreCase(this.clientScope.getId()));
                if (isRealmDefault) {
                    throw ErrorResponse.error("This Client Scope can't be made parameterized as it's assigned as a Realm Default Scope",
                            Response.Status.BAD_REQUEST);
                }
            }
            // after the previous validation, run the usual Parameterized Scope validations.
            validateParameterizedClientScope(session, rep);
        }
