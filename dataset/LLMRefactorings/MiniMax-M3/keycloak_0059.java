public class keycloak_0059 {

        private static void runPolicy(ComponentModel policyModel, KeycloakSession session, ClientRegistrationProvider provider,
                               String opDescription, ClientRegOperation op) throws ClientRegistrationPolicyException {
            ClientRegistrationPolicy policy = getPolicy(policyModel, session);

            if (logger.isTraceEnabled()) {
                logger.tracef("Running policy '%s' %s", policyModel.getName(), opDescription);
            }

            try {
                op.run(policy);
            } catch (ClientRegistrationPolicyException crpe) {
                handlePolicyException(policyModel, provider, opDescription, crpe);
                throw crpe;
            }
        }

        private static ClientRegistrationPolicy getPolicy(ComponentModel policyModel, KeycloakSession session) throws ClientRegistrationPolicyException {
            ClientRegistrationPolicy policy = session.getProvider(ClientRegistrationPolicy.class, policyModel);
            if (policy == null) {
                throw new ClientRegistrationPolicyException("Policy of type '" + policyModel.getProviderId() + "' not found");
            }
            return policy;
        }

        private static void handlePolicyException(ComponentModel policyModel, ClientRegistrationProvider provider,
                                                  String opDescription, ClientRegistrationPolicyException crpe) {
            provider.getEvent().detail(Details.CLIENT_REGISTRATION_POLICY, policyModel.getName());
            crpe.setPolicyModel(policyModel);
            ServicesLogger.LOGGER.clientRegistrationRequestRejected(opDescription, crpe.getMessage());
        }
}
