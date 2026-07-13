public class keycloak_0166 {

    @Override
    public boolean isImpersonatable(UserModel user, ClientModel requester) {
        ResourceServer server = root.realmResourceServer();

        if (server == null) {
            return true;
        }

        Resource resource =  resourceStore.findByName(server, USERS_RESOURCE);

        if (resource == null) {
            return true;
        }

        Policy policy = authz.getStoreFactory().getPolicyStore().findByName(server, USER_IMPERSONATED_PERMISSION);

        if (policy == null) {
            return true;
        }

        Set<Policy> associatedPolicies = policy.getAssociatedPolicies();
        // if no policies attached to permission then just do default behavior
        if (associatedPolicies == null || associatedPolicies.isEmpty()) {
            return true;
        }

        Map<String, List<String>> additionalClaims = buildAdditionalClaims(requester);

        return hasPermission(new DefaultEvaluationContext(new UserModelIdentity(root.realm, user), additionalClaims, session), USER_IMPERSONATED_SCOPE);
    }

    private Map<String, List<String>> buildAdditionalClaims(ClientModel requester) {
        Map<String, List<String>> additionalClaims = Collections.emptyMap();
        if (requester != null) {
            // make sure the requesting client id is available from the context as we are using a user identity that does not rely on token claims
            additionalClaims = new HashMap<>();
            additionalClaims.put("kc.client.id", Arrays.asList(requester.getClientId()));
        }
        return additionalClaims;
    }
}
