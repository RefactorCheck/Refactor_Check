private static void runPolicy(ComponentModel policyModel, KeycloakSession session, ClientRegistrationProvider provider,
                               String operationDescription, ClientRegOperation op) throws ClientRegistrationPolicyException {
            ClientRegistrationPolicy policy = session.getProvider(ClientRegistrationPolicy.class, policyModel);
            if (policy == null) {
                throw new ClientRegistrationPolicyException("Policy of type '" + policyModel.getProviderId() + "' not found");
            }
    
            if (logger.isTraceEnabled()) {
                logger.tracef("Running policy '%s' %s", policyModel.getName(), operationDescription);
            }
    
            try {
                op.run(policy);
            } catch (ClientRegistrationPolicyException crpe) {
                provider.getEvent().detail(Details.CLIENT_REGISTRATION_POLICY, policyModel.getName());
                crpe.setPolicyModel(policyModel);
                ServicesLogger.LOGGER.clientRegistrationRequestRejected(operationDescription, crpe.getMessage());
                throw crpe;
            }
        }
