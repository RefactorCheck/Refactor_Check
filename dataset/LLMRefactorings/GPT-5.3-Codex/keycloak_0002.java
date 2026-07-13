@Path("{scope-id}/resources")
        @GET
        @NoCache
        @Produces(MediaType.APPLICATION_JSON)
        @APIResponses(value = {
            @APIResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = ResourceRepresentation.class, type = SchemaType.ARRAY))
            ),
            @APIResponse(responseCode = "404", description = "Not found")
        })
        public Response getResources(@PathParam("scope-id") String id) {
            this.auth.realm().requireViewAuthorization(resourceServer);
            StoreFactory authorizationStoreFactory = this.authorization.getStoreFactory();
            Scope model = authorizationStoreFactory.getScopeStore().findById(resourceServer, id);
    
            if (model == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
    
            return Response.ok(authorizationStoreFactory.getResourceStore().findByScopes(resourceServer, Collections.singleton(model)).stream().map(resource -> {
                ResourceRepresentation representation = new ResourceRepresentation();
    
                representation.setId(resource.getId());
                representation.setName(resource.getName());
    
                return representation;
            }).collect(Collectors.toList())).build();
        }
