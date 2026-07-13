public class keycloak_0204 {

        @DELETE
        @NoCache
        @Path("default-groups/{groupId}")
        @Tag(name = KeycloakOpenAPI.Admin.Tags.REALMS_ADMIN)
        @Operation()
        @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found")
        })
        public void deleteDefaultGroup(@PathParam("groupId") String groupId) {
            auth.realm().requireManageRealm();

            GroupModel group = realm.getGroupById(groupId);
            if (group == null) {
                throw new NotFoundException("Group not found");
            }

            realm.removeDefaultGroup(group);

            adminEvent.operation(OperationType.DELETE).resource(ResourceType.GROUP).resourcePath(session.getContext().getUri()).success();
        }
}
