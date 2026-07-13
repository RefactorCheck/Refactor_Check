public class keycloak_0281 {

        @POST
        @NoCache
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        @APIResponses(value = {
            @APIResponse(
                responseCode = "201", description = "Created",
                content = @Content(schema = @Schema(implementation = ResourceRepresentation.class))
            ),
            @APIResponse(responseCode = "400", description = "Bad Request")
        })
        public Response createPost(ResourceRepresentation resource) {
            if (resource == null) {
                Response badRequest = Response.status(Status.BAD_REQUEST).build();
                return badRequest;
            }

            ResourceRepresentation newResource = create(resource);

            audit(resource, resource.getId(), OperationType.CREATE);

            Response createdResponse = Response.status(Status.CREATED).entity(newResource).build();
            return createdResponse;
        }
}
