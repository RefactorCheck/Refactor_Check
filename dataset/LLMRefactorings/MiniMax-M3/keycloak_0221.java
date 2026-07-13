public class keycloak_0221 {

        @Path("count")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ORGANIZATIONS)
        @Operation( summary = "Returns number of members in the organization.")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Long.class))),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public Long count() {
            auth.users().requireQuery();
    
            if (isUnauthorizedToViewMembers()) {
                return 0L;
            }
    
            return provider.getMembersCount(organization);
        }

        private boolean isUnauthorizedToViewMembers() {
            return !AdminPermissionsSchema.SCHEMA.isAdminPermissionsEnabled(realm) && !auth.users().canView();
        }
}
