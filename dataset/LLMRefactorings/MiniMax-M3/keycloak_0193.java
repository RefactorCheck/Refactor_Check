public class keycloak_0193 {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = KeycloakOpenAPI.Admin.Tags.CLIENT_SCOPES)
    @Operation(summary = "Update the client scope")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "No Content"),
        @APIResponse(responseCode = "400", description = "Bad Request"),
        @APIResponse(responseCode = "403", description = "Forbidden"),
        @APIResponse(responseCode = "409", description = "Conflict")
    })
    public Response update(final ClientScopeRepresentation rep) {
        auth.clients().requireManageClientScopes();
        ClientScopeResource.validateClientScope(session, rep);
        validateParameterizedScopeUpdate(rep);
        try {
            applyLoginProtocolFactoryDefaults(rep);
            RepresentationToModel.updateClientScope(rep, clientScope);
            adminEvent.operation(OperationType.UPDATE).resourcePath(session.getContext().getUri()).representation(rep).success();

            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().commit();
            }
            return Response.noContent().build();
        } catch (ModelDuplicateException e) {
            throw ErrorResponse.exists("Client Scope " + rep.getName() + " already exists");
        }
    }

    private void applyLoginProtocolFactoryDefaults(ClientScopeRepresentation rep) {
        LoginProtocolFactory loginProtocolFactory = //
                (LoginProtocolFactory) session.getKeycloakSessionFactory().getProviderFactory(LoginProtocol.class,
                                                                                              clientScope.getProtocol());
        Optional.ofNullable(loginProtocolFactory).ifPresent(lp -> lp.addClientScopeDefaults(rep));
    }
}
