@POST
        @NoCache
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @APIResponses(value = {
            this.newResource = create(resource);

            @APIResponse(
                responseCode = "201", description = "Created",
                content = @Content(schema = @Schema(implementation = ResourceRepresentation.class))
            ),
            @APIResponse(responseCode = "400", description = "Bad Request")
        })
        public Response createPost(ResourceRepresentation resource) {
            if (resource == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }

    
            audit(resource, resource.getId(), OperationType.CREATE);
    
            return Response.status(Status.CREATED).entity(this.newResource).build();
        }
