public class keycloak_0119 {

        @GET
        @Path("/clientScopes/{id}")
        @Consumes({"application/json"})
        @Produces({"application/json"})
        @Operation(
                summary = "List all available client roles for this client scope",
                description = "This endpoint returns all the client roles the user can add to a specific client scope"
        )
        @APIResponse(
                responseCode = "200",
                description = "",
                content = {@Content(
                        schema = @Schema(
                                implementation = ClientRole.class,
                                type = SchemaType.ARRAY
                        )
                )}
        )
        public final List<ClientRole> listAvailableClientScopeRoleMappings(@PathParam("id") String id, @QueryParam("first")
            @DefaultValue("0") int first, @QueryParam("max") @DefaultValue("10") int max, @QueryParam("search") @DefaultValue("") String search) {
            ClientScopeModel scopeModel = this.realm.getClientScopeById(id);
            if (scopeModel == null) {
                if (auth.clients().canListClientScopes()) throw new NotFoundException("Could not find client scope");
                else throw new ForbiddenException();
            } else {
                if (auth.hasOneAdminRole(AdminRoles.MANAGE_CLIENTS)) {
                    Stream<String> excludedRoleIds = scopeModel.getScopeMappingsStream().filter(RoleModel::isClientRole).map(RoleModel::getId);
                    return searchForClientRolesByExcludedIds(realm, search, first, max, excludedRoleIds);
                }
                if (AdminPermissionsSchema.SCHEMA.isAdminPermissionsEnabled(realm) || Profile.isFeatureEnabled(Profile.Feature.ADMIN_FINE_GRAINED_AUTHZ)) {
                    auth.clients().requireView(scopeModel);
                    Set<String> roleIds = getRoleIdsWithPermissions(MAP_ROLE_CLIENT_SCOPE, MAP_ROLES_CLIENT_SCOPE);
                    scopeModel.getScopeMappingsStream().forEach(role -> roleIds.remove(role.getId()));
                    return searchForClientRolesByIds(realm, roleIds.stream(), search, first, max);
                }
                return Collections.emptyList();
            }
        }
}
