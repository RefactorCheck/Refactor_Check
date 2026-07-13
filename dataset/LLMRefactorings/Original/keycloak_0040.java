public class keycloak_0040 {

        @POST
        @Path("mappers")
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.IDENTITY_PROVIDERS)
        @Operation( summary = "Add a mapper to identity provider")
        public Response addMapper(IdentityProviderMapperRepresentation mapper) {
            this.auth.realm().requireManageIdentityProviders();
    
            if (identityProviderModel == null) {
                throw new jakarta.ws.rs.NotFoundException();
            }
    
            IdentityProviderMapperModel model = RepresentationToModel.toModel(mapper);
    
            try {
    //            model = realm.addIdentityProviderMapper(model);
                model = session.identityProviders().createMapper(model);
            } catch (Exception e) {
                throw ErrorResponse.error("Failed to add mapper '" + model.getName() + "' to identity provider [" + identityProviderModel.getProviderId() + "].", Response.Status.BAD_REQUEST);
            }
    
            adminEvent.operation(OperationType.CREATE).resource(ResourceType.IDENTITY_PROVIDER_MAPPER).resourcePath(session.getContext().getUri(), model.getId())
                .representation(mapper).success();
    
            return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(model.getId()).build()).build();
    
        }
}
