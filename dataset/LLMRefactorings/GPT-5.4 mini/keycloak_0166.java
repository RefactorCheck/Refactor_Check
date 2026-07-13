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
            boolean noAssociatedPolicies = associatedPolicies == null || associatedPolicies.isEmpty();
            if (noAssociatedPolicies) {
                return true;
            }

            Map<String, List<String>> additionalClaims = Collections.emptyMap();

            if (requester != null) {
                additionalClaims = new HashMap<>();
                additionalClaims.put("kc.client.id", Arrays.asList(requester.getClientId()));
            }

            return hasPermission(new DefaultEvaluationContext(new UserModelIdentity(root.realm, user), additionalClaims, session), USER_IMPERSONATED_SCOPE);
        }
}
