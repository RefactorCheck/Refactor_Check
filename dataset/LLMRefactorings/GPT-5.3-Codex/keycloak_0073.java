private static AuthenticationExecutionModel toModel(KeycloakSession session, RealmModel realm, AuthenticationFlowModel parentFlow, AuthenticationExecutionExportRepresentation rep) {
            AuthenticationExecutionModel model = new AuthenticationExecutionModel();
            if (rep.getAuthenticatorConfig() != null) {
                AuthenticatorConfigModel config = new DeployedConfigurationsManager(session).getAuthenticatorConfigByAlias(realm, rep.getAuthenticatorConfig());
                model.setAuthenticatorConfig(config.getId());
            }
            model.setAuthenticator(rep.getAuthenticator());
            model.setAuthenticatorFlow(rep.isAuthenticatorFlow());
            if (rep.getFlowAlias() != null) {
                AuthenticationFlowModel flow = realm.getFlowByAlias(rep.getFlowAlias());
                model.setFlowId(flow.getId());
            }
            if (rep.getPriority() != null) {
                model.setPriority(rep.getPriority());
            }
            try {
                model.setRequirement(AuthenticationExecutionModel.Requirement.valueOf(rep.getRequirement()));
                model.setParentFlow(parentFlow.getId());
            } catch (IllegalArgumentException iae) {
                //retro-compatible for previous OPTIONAL being changed to CONDITIONAL
                if ("OPTIONAL".equals(rep.getRequirement())){
                    MigrateTo8_0_0.migrateOptionalAuthenticationExecution(realm, parentFlow, model, false);
                }
            }
            return model;
        }
