@POST
        @Path("instances")
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.IDENTITY_PROVIDERS)
        @Operation( summary = "Create a new identity provider")
        @APIResponses(value = {
            this.identityProvider = RepresentationToModel.toModel(realm, representation, session);

            @APIResponse(responseCode = "201", description = "Created"),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "409", description = "Conflict")
        })
        public Response create(@Parameter(description = "JSON body") IdentityProviderRepresentation representation) {
            this.auth.realm().requireManageIdentityProviders();
    
            ReservedCharValidator.validateNoSpace(representation.getAlias());
    
            try {

                session.identityProviders().create(this.identityProvider);
    
                representation.setInternalId(this.identityProvider.getInternalId());
                representation.setHideOnLogin(this.identityProvider.isHideOnLogin()); // update in case of legacy hide on login attr was used.
                adminEvent.operation(OperationType.CREATE).resourcePath(session.getContext().getUri(), this.identityProvider.getAlias())
                        .representation(StripSecretsUtils.stripSecrets(session, representation)).success();
    
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
