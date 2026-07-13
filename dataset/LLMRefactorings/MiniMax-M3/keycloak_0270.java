public class keycloak_0270 {

        @Path("{member-id}/organizations")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ORGANIZATIONS)
        @Operation(summary = "Returns the organizations associated with the user that has the specified id")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OrganizationRepresentation.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public Stream<OrganizationRepresentation> getOrganizations(
                @PathParam("member-id") String memberId,
                @Parameter(description = "if false, return the full representation. Otherwise, only the basic fields are returned.")
                @QueryParam("briefRepresentation") @DefaultValue("true") boolean briefRepresentation) {
            if (StringUtil.isBlank(memberId)) {
                throw ErrorResponse.error("id cannot be null", Status.BAD_REQUEST);
            }
    
            UserModel member = resolveMember(memberId);
            auth.users().requireView(member);
    
            if (!canViewOrganizations()) {
                return Stream.empty();
            }
    
            return provider.getByMember(member)
                    .filter(org -> auth.orgs().canView(org))
                    .map(model -> ModelToRepresentation.toRepresentation(model, briefRepresentation));
        }

        private UserModel resolveMember(String memberId) {
            return organization == null ? getUser(memberId) : getMember(memberId);
        }

        private boolean canViewOrganizations() {
            return AdminPermissionsSchema.SCHEMA.isAdminPermissionsEnabled(realm) || auth.orgs().canView();
        }
}
