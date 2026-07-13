public class keycloak_0040 {

        @POST
        @Path("mappers")
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.IDENTITY_PROVIDERS)
        @Operation( summary = "Add a mapper to identity provider")
        public Response addMapper(IdentityProviderMapperRepresentation mapperRepresentation) {
            this.auth.realm().requireManageIdentityProviders();
    
            if (identityProviderModel == null) {
                throw new jakarta.ws.rs.NotFoundException();
            }
    
            IdentityProviderMapperModel mapperModel = RepresentationToModel.toModel(mapperRepresentation);
    
            try {
    //            model = realm.addIdentityProviderMapper(model);
                mapperModel = session.identityProviders().createMapper(mapperModel);
            } catch (Exception e) {
                throw ErrorResponse.error("Failed to add mapper '" + mapperModel.getName() + "' to identity provider [" + identityProviderModel.getProviderId() + "].", Response.Status.BAD_REQUEST);
            }
    
            adminEvent.operation(OperationType.CREATE).resource(ResourceType.IDENTITY_PROVIDER_MAPPER).resourcePath(session.getContext().getUri(), mapperModel.getId())
                .representation(mapperRepresentation).success();
    
            return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(mapperModel.getId()).build()).build();
    
        }
}
