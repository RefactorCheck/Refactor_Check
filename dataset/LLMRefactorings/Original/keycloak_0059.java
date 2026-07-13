public class keycloak_0059 {

        private static void runPolicy(ComponentModel policyModel, KeycloakSession session, ClientRegistrationProvider provider,
                               String opDescription, ClientRegOperation op) throws ClientRegistrationPolicyException {
            ClientRegistrationPolicy policy = session.getProvider(ClientRegistrationPolicy.class, policyModel);
            if (policy == null) {
                throw new ClientRegistrationPolicyException("Policy of type '" + policyModel.getProviderId() + "' not found");
            }
    
            if (logger.isTraceEnabled()) {
                logger.tracef("Running policy '%s' %s", policyModel.getName(), opDescription);
            }
    
            try {
                op.run(policy);
            } catch (ClientRegistrationPolicyException crpe) {
                provider.getEvent().detail(Details.CLIENT_REGISTRATION_POLICY, policyModel.getName());
                crpe.setPolicyModel(policyModel);
                ServicesLogger.LOGGER.clientRegistrationRequestRejected(opDescription, crpe.getMessage());
                throw crpe;
            }
        }
}
