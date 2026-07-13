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

            String alias = representation.getAlias();
            ReservedCharValidator.validateNoSpace(alias);

            try {
                IdentityProviderModel identityProvider = RepresentationToModel.toModel(realm, representation, session);
                session.identityProviders().create(identityProvider);

                representation.setInternalId(identityProvider.getInternalId());
                representation.setHideOnLogin(identityProvider.isHideOnLogin()); // update in case of legacy hide on login attr was used.
                adminEvent.operation(OperationType.CREATE).resourcePath(session.getContext().getUri(), alias)
                        .representation(StripSecretsUtils.stripSecrets(session, representation)).success();

                return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(alias).build()).build();
            } catch (IllegalArgumentException e) {
                String message = e.getMessage();

                if (message == null) {
                    message = "Invalid request";
                }

                throw ErrorResponse.error(message, BAD_REQUEST);
            } catch (ModelDuplicateException e) {
                throw ErrorResponse.exists("Identity Provider " + alias + " already exists");
            }
        }
}
