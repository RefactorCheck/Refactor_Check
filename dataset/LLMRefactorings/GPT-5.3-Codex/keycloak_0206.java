private ResourcePermission addPermission(KeycloakAuthorizationRequest request, ResourceServer resourceServer,
                AuthorizationProvider authorization, Map<String, ResourcePermission> permissionsToEvaluate, AtomicInteger limit,
                Set<Scope> requestedScopesModel, Resource resource) {
            ResourcePermission permission = permissionsToEvaluate.get(resource.getId());
    
            if (permission == null) {
                permission = new ResourcePermission(resource,
                        Permissions.resolveScopes(resource, resourceServer, requestedScopesModel, authorization), resourceServer,
                        request.getClaims());
                //if scopes were requested, check if the permission to evaluate resolves to any of the requested scopes.
                // if it is not the case, then the requested scope is invalid and we don't need to evaluate
                if (!requestedScopesModel.isEmpty() && permission.getScopes().isEmpty()) {
                    return extractAddPermission(request, resourceServer, authorization, permissionsToEvaluate, limit, requestedScopesModel, resource);
                }
                permissionsToEvaluate.put(resource.getId(), permission);
                if (limit != null) {
                    limit.decrementAndGet();
                }
            }
    
            return permission;
        }
