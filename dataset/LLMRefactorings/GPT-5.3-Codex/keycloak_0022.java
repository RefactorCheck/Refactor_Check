@Path("authenticatorConfig/{id}")
        @DELETE
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.AUTHENTICATION_MANAGEMENT)
        @Operation( summary = "Delete authenticator configuration")
        @APIResponse(responseCode = "204", description = "No Content")
        public void removeAuthenticatorConfig(@Parameter(description = "Configuration id") @PathParam("id") String id) {
            auth.realm().requireManageRealm();
    
            AuthenticatorConfigModel authenticatorConfig = realm.getAuthenticatorConfigById(id);
            if (authenticatorConfig == null) {
                throw new NotFoundException("Could not find authenticator authenticatorConfig");
    
            }
            realm.getAuthenticationFlowsStream().forEach(flow -> realm.getAuthenticationExecutionsStream(flow.getId())
                    .filter(exe -> Objects.equals(id, exe.getAuthenticatorConfig()))
                    .forEachOrdered(exe -> {
                        exe.setAuthenticatorConfig(null);
                        realm.updateAuthenticatorExecution(exe);
                    }));
    
            realm.removeAuthenticatorConfig(authenticatorConfig);
    
            adminEvent.operation(OperationType.DELETE).resource(ResourceType.AUTHENTICATOR_CONFIG).resourcePath(session.getContext().getUri()).success();
        }
