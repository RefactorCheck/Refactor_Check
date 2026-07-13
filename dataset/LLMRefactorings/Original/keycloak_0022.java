public class keycloak_0022 {

        @Path("config/{id}")
        @DELETE
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.AUTHENTICATION_MANAGEMENT)
        @Operation( summary = "Delete authenticator configuration")
        @APIResponse(responseCode = "204", description = "No Content")
        public void removeAuthenticatorConfig(@Parameter(description = "Configuration id") @PathParam("id") String id) {
            auth.realm().requireManageRealm();
    
            AuthenticatorConfigModel config = realm.getAuthenticatorConfigById(id);
            if (config == null) {
                throw new NotFoundException("Could not find authenticator config");
    
            }
            realm.getAuthenticationFlowsStream().forEach(flow -> realm.getAuthenticationExecutionsStream(flow.getId())
                    .filter(exe -> Objects.equals(id, exe.getAuthenticatorConfig()))
                    .forEachOrdered(exe -> {
                        exe.setAuthenticatorConfig(null);
                        realm.updateAuthenticatorExecution(exe);
                    }));
    
            realm.removeAuthenticatorConfig(config);
    
            adminEvent.operation(OperationType.DELETE).resource(ResourceType.AUTHENTICATOR_CONFIG).resourcePath(session.getContext().getUri()).success();
        }
}
