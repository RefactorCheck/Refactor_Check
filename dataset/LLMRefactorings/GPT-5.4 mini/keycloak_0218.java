public class keycloak_0218 {

        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.IDENTITY_PROVIDERS)
        @Operation( summary = "Update the identity provider")
        @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "409", description = "Conflict")
        })
        public Response update(IdentityProviderRepresentation providerRep) {
            this.auth.realm().requireManageIdentityProviders();

            if (identityProviderModel == null) {
                throw new jakarta.ws.rs.NotFoundException();
            }

            try {
                updateIdpFromRep(providerRep, realm, session);

                adminEvent.operation(OperationType.UPDATE).resourcePath(session.getContext().getUri()).representation(providerRep).success();

                return Response.noContent().build();
            } catch (IllegalArgumentException e) {
                throw ErrorResponse.error(e.getMessage() == null ? "Invalid request" : e.getMessage(), BAD_REQUEST);
            } catch (ModelDuplicateException e) {
                throw ErrorResponse.exists("Identity Provider " + providerRep.getAlias() + " already exists");
            }
        }
}
