public class keycloak_0045 {

        @Path("/search")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @APIResponses(value = {
            @APIResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = AbstractPolicyRepresentation.class))
            ),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "400", description = "Bad Request")
        })
        public Response findByName(@QueryParam("name") String name, @QueryParam("fields") String fields) {
            if (auth != null) {
                this.auth.realm().requireViewAuthorization(resourceServer);
            }

            if (name == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }

            Policy model = authorization.getStoreFactory().getPolicyStore().findByName(this.resourceServer, name);

            if (model == null) {
                return Response.noContent().build();
            }

            return Response.ok(toRepresentation(model, fields, authorization)).build();
        }
}
