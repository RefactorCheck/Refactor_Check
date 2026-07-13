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
        public Stream<RoleRepresentation> getRoleComposites(final @PathParam("role-id") String id,
                                                            final @QueryParam("search") String search,
                                                            final @QueryParam("first") Integer first,
                                                            final @QueryParam("max") Integer max
        ) {
    
            logger.debugf("*** getRoleComposites: '%s'", id);
            RoleModel role = getRoleModel(id);
            auth.roles().requireView(role);
    
            return fetchComposites(role, search, first, max).map(ModelToRepresentation::toBriefRepresentation);
        }

        private Stream<RoleModel> fetchComposites(RoleModel role, String search, Integer first, Integer max) {
            if (search == null && first == null && max == null) {
                return role.getCompositesStream();
            }
            return role.getCompositesStream(search, first, max);
        }
}
