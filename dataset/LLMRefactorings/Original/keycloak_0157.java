public class keycloak_0157 {

        @Path("{role-id}/composites/clients/{clientUuid}")
        @GET
        @NoCache
        @Produces(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ROLES_BY_ID)
        @Operation(summary = "Get client-level roles for the client that are in the role's composite")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = RoleRepresentation.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found")
        })
        public Stream<RoleRepresentation> getClientRoleComposites(final @PathParam("role-id") String id,
                                                                    final @PathParam("clientUuid") String clientUuid) {
    
            RoleModel role = getRoleModel(id);
            auth.roles().requireView(role);
            ClientModel clientModel = realm.getClientById(clientUuid);
            if (clientModel == null) {
                throw new NotFoundException("Could not find client");
            }
            return getClientRoleComposites(clientModel, role);
        }
}
