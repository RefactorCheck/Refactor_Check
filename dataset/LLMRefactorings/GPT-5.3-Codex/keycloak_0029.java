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
                                                            final @QueryParam("roleSearch") String roleSearch,
                                                            final @QueryParam("first") Integer first,
                                                            final @QueryParam("max") Integer max
        ) {
    
            logger.debugf("*** getRoleComposites: '%s'", id);
            RoleModel role = getRoleModel(id);
            auth.roles().requireView(role);
    
            if (roleSearch == null && first == null && max == null) {
                return role.getCompositesStream().map(ModelToRepresentation::toBriefRepresentation);
            }
    
            return role.getCompositesStream(roleSearch, first, max).map(ModelToRepresentation::toBriefRepresentation);
        }
