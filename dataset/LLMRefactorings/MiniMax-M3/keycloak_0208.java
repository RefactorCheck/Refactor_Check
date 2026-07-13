public class keycloak_0208 {

    @POST
    @Path("instances")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = KeycloakOpenAPI.Admin.Tags.IDENTITY_PROVIDERS)
    @Operation( summary = "Create a new identity provider")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Created"),
        @APIResponse(responseCode = "400", description = "Bad Request"),
        @APIResponse(responseCode = "409", description = "Conflict")
    })
    public Response create(@Parameter(description = "JSON body") IdentityProviderRepresentation representation) {
        this.auth.realm().requireManageIdentityProviders();

        ReservedCharValidator.validateNoSpace(representation.getAlias());

        try {
            createIdentityProvider(representation);
            return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(representation.getAlias()).build()).build();
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();

            if (message == null) {
                message = "Invalid request";
            }

            throw ErrorResponse.error(message, BAD_REQUEST);
        } catch (ModelDuplicateException e) {
            throw ErrorResponse.exists("Identity Provider " + representation.getAlias() + " already exists");
        }
    }

    private void createIdentityProvider(IdentityProviderRepresentation representation) {
        IdentityProviderModel identityProvider = RepresentationToModel.toModel(realm, representation, session);
        session.identityProviders().create(identityProvider);

        representation.setInternalId(identityProvider.getInternalId());
        representation.setHideOnLogin(identityProvider.isHideOnLogin());
        adminEvent.operation(OperationType.CREATE).resourcePath(session.getContext().getUri(), identityProvider.getAlias())
                .representation(StripSecretsUtils.stripSecrets(session, representation)).success();
    }
}
