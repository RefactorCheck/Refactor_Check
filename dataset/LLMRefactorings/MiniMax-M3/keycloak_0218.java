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
                handleIllegalArgument(e);
            } catch (ModelDuplicateException e) {
                throw ErrorResponse.exists("Identity Provider " + providerRep.getAlias() + " already exists");
            }
        }

        private void handleIllegalArgument(IllegalArgumentException e) {
            String message = e.getMessage();

            if (message == null) {
                message = "Invalid request";
            }

            throw ErrorResponse.error(message, BAD_REQUEST);
        }
}
