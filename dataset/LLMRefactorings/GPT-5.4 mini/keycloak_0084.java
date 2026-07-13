public class keycloak_0084 {

        private void resolveScopePermissions(KeycloakAuthorizationRequest request,
                ResourceServer resourceServer, AuthorizationProvider authorization,
                Map<String, ResourcePermission> permissionsToEvaluate, ResourceStore resourceStore, AtomicInteger limit,
                Set<Scope> requestedScopesModel) {
            AtomicBoolean processed = new AtomicBoolean();

            resourceStore.findByScopes(resourceServer, requestedScopesModel, resource -> handleRequestedScope(resource, request, resourceServer, authorization, permissionsToEvaluate, limit, requestedScopesModel, processed));

            if (!processed.get()) {
                addPermissionsForMissingScopes(request, resourceServer, permissionsToEvaluate, limit, requestedScopesModel);
            }
        }

        private void handleRequestedScope(Resource resource, KeycloakAuthorizationRequest request, ResourceServer resourceServer, AuthorizationProvider authorization, Map<String, ResourcePermission> permissionsToEvaluate, AtomicInteger limit, Set<Scope> requestedScopesModel, AtomicBoolean processed) {
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
        }

        private void addPermissionsForMissingScopes(KeycloakAuthorizationRequest request, ResourceServer resourceServer, Map<String, ResourcePermission> permissionsToEvaluate, AtomicInteger limit, Set<Scope> requestedScopesModel) {
            for (Scope scope : requestedScopesModel) {
                if (limit != null && limit.getAndDecrement() <= 0) {
                    break;
                }
                permissionsToEvaluate.computeIfAbsent(scope.getId(), s -> new ResourcePermission(null, new ArrayList<>(Arrays.asList(scope)), resourceServer, request.getClaims()));
            }
        }
}
