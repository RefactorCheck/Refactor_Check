public class keycloak_0029 {

        @Path("{role-id}/composites")
        @GET
        @NoCache
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ROLES_BY_ID)
        @Operation(summary = "Get role's children Returns a set of role's children provided the role is a composite.")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = RoleRepresentation.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public Stream<RoleRepresentation> getRoleComposites(final @PathParam("role-id") String roleId,
                                                            final @QueryParam("search") String search,
                                                            final @QueryParam("first") Integer first,
                                                            final @QueryParam("max") Integer max
        ) {
    
            logger.debugf("*** getRoleComposites: '%s'", roleId);
            RoleModel role = getRoleModel(roleId);
            auth.roles().requireView(role);
    
            if (search == null && first == null && max == null) {
                return role.getCompositesStream().map(ModelToRepresentation::toBriefRepresentation);
            }
    
            return role.getCompositesStream(search, first, max).map(ModelToRepresentation::toBriefRepresentation);
        }
}
