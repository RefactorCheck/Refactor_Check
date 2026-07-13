public class keycloak_0198 {

        @Path("/flows/{flowAlias}/copy")
        @POST
        @NoCache
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.AUTHENTICATION_MANAGEMENT)
        @Operation( summary = "Copy existing authentication flow under a new name The new name is given as 'newName' attribute of the passed JSON object")
        @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created"),
            @APIResponse(responseCode = "409", description = "Conflict")
        })
        public Response copy(@Parameter(description="name of the existing authentication flow") @PathParam("flowAlias") String flowAlias, Map<String, String> data) {
            auth.realm().requireManageRealm();
    
            String newName = data.get("newName");
            ReservedCharValidator.validate(newName);
            if (realm.getFlowByAlias(newName) != null) {
                throw ErrorResponse.exists("New flow alias name already exists");
            }
    
            AuthenticationFlowModel flow = realm.getFlowByAlias(flowAlias);
            if (flow == null) {
                logger.debugf("flow not found: %s", flowAlias);
                throw new NotFoundException("Flow not found");
            }
    
            AuthenticationFlowModel copy = copyFlow(session, realm, flow, newName);
    
            data.put("id", copy.getId());
            adminEvent.operation(OperationType.CREATE).resourcePath(session.getContext().getUri()).representation(data).success();
    
            return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(copy.getId()).build()).build();
        }
}
