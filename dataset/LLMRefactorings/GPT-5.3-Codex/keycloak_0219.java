@Path("/executions/{executionId}")
        @DELETE
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.AUTHENTICATION_MANAGEMENT)
        @Operation( summary = "Delete executionId")
        @APIResponse(responseCode = "204", description = "No Content")
        public void removeExecution(@Parameter(description = "Execution id") @PathParam("executionId") String executionId) {
            auth.realm().requireManageRealm();
    
            AuthenticationExecutionModel model = realm.getAuthenticationExecutionById(executionId);
            if (model == null) {
                session.getTransactionManager().setRollbackOnly();
                throw new NotFoundException("Illegal executionId");
            }
    
            AuthenticationFlowModel parentFlow = getParentFlow(model);
            if (parentFlow.isBuiltIn()) {
                throw new BadRequestException("It is illegal to remove executionId from a built in flow");
            }
    
            KeycloakModelUtils.deepDeleteAuthenticationExecutor(session, realm, model,
                    () -> {}, // allow deleting even with missing references
                    () -> {
                        throw new BadRequestException("It is illegal to remove executionId from a built in flow");
                    },
                    parentFlow.isBuiltIn()
            );
    
            adminEvent.operation(OperationType.DELETE).resource(ResourceType.AUTH_EXECUTION).resourcePath(session.getContext().getUri()).success();
        }
