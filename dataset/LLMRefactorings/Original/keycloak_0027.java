public class keycloak_0027 {

        @Path("{role-name}/users")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ROLES)
        @Operation(summary = "Returns a stream of users that have the specified role name.")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UserRepresentation.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found")
        })
        public Stream<UserRepresentation> getUsersInRole(final @Parameter(description = "the role name.") @PathParam("role-name") String roleName,
                                                        @Parameter(description = "Boolean which defines whether brief representations are returned (default: false)") @QueryParam("briefRepresentation") Boolean briefRepresentation,
                                                        @Parameter(description = "first result to return. Ignored if negative or {@code null}.") @QueryParam("first") Integer firstResult,
                                                        @Parameter(description = "Maximum number of results to return. Unbounded if negative.") @QueryParam("max") @DefaultValue(Constants.DEFAULT_MAX_RESULTS_STR) Integer maxResults) {
    
            auth.roles().requireView(roleContainer);
            auth.users().requireQuery();
    
            firstResult = firstResult != null ? firstResult : 0;
            maxResults = maxResults != null ? maxResults : Constants.DEFAULT_MAX_RESULTS;
    
            RoleModel role = roleContainer.getRole(roleName);
            if (role == null) {
                throw new NotFoundException("Could not find role");
            }
    
            boolean briefRep = Boolean.TRUE.equals(briefRepresentation);
    
            return session.users().getRoleMembersStream(realm, role, firstResult, maxResults)
                    .map((u) -> ModelToRepresentation.toRepresentation(session, u, briefRep));
        }
}
