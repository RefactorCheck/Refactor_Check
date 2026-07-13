private static void resolveScopePermissions(KeycloakAuthorizationRequest request,
                ResourceServer resourceServer, AuthorizationProvider authorization,
                Map<String, ResourcePermission> permissionsToEvaluate, ResourceStore resourceStore, AtomicInteger limit,
                Set<Scope> requestedScopesModel) {
            AtomicBoolean processed = new AtomicBoolean();
    
            resourceStore.findByScopes(resourceServer, requestedScopesModel, resource -> {
                if (limit != null && limit.get() <= 0) {
                    return;
                }
    
                ResourcePermission perm = permissionsToEvaluate.get(resource.getId());
    
                if (perm == null) {
                    perm = Permissions.createResourcePermissions(resource, resourceServer, requestedScopesModel, authorization, request);
                    permissionsToEvaluate.put(resource.getId(), perm);
                    if (limit != null) {
                        limit.decrementAndGet();
                    }
                } else {
                    for (Scope scope : requestedScopesModel) {
                        perm.addScope(scope);
                    }
                }
    
                processed.compareAndSet(false, true);
            });
    
            if (!processed.get()) {
                for (Scope scope : requestedScopesModel) {
                    if (limit != null && limit.getAndDecrement() <= 0) {
                        break;
                    }
                    permissionsToEvaluate.computeIfAbsent(scope.getId(), s -> new ResourcePermission(null, new ArrayList<>(Arrays.asList(scope)), resourceServer, request.getClaims()));
                }
            }
        }
