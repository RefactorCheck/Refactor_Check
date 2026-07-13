public class keycloak_0068 {

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ORGANIZATIONS)
        @Operation(summary = "Adds the user with the specified id as a member of the organization", description = "Adds, or associates, " +
                "an existing user with the organization. If no user is found, or if it is already associated with the organization, " +
                "an error response is returned")
        @RequestBody(description = "Payload should contain only id of the user to be added to the organization (UUID with or without quotes). " +
                "Surrounding whitespace characters will be trimmed.", required = true)
        @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created"),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "409", description = "Conflict")
        })
        public Response addMember(String id) {
            auth.orgs().requireManage(organization);
            id = id.trim().replaceAll("^\"|\"$", ""); // fixes https://github.com/keycloak/keycloak/issues/34401
    
            UserModel user = getUser(id);
            auth.users().requireManage(user);
    
            try {
                if (provider.addMember(organization, user)) {
                    adminEvent.operation(OperationType.CREATE).resource(ResourceType.ORGANIZATION_MEMBERSHIP)
                            .representation(ModelToRepresentation.toRepresentation(organization))
                            .resourcePath(session.getContext().getUri())
                            .detail(UserModel.USERNAME, user.getUsername())
                            .detail(UserModel.EMAIL, user.getEmail())
                            .success();
                    return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(user.getId()).build()).build();
                }
            } catch (ModelException me) {
                throw ErrorResponse.error(me.getMessage(), Status.BAD_REQUEST);
            }
    
            throw ErrorResponse.error("User is already a member of the organization.", Status.CONFLICT);
        }
}
