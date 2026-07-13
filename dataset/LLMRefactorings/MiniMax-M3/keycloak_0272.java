public class keycloak_0272 {

    private static final String SCOPES_CONFIG_KEY = "scopes";
    private static final String RESOURCES_CONFIG_KEY = "resources";
    private static final String APPLY_POLICIES_CONFIG_KEY = "applyPolicies";

    private static PolicyRepresentation toRepresentation(AuthorizationProvider authorizationProvider, Policy policy) {
        try {
            PolicyRepresentation rep = toRepresentation(policy, authorizationProvider, true, true);

            Map<String, String> config = new HashMap<>(rep.getConfig());

            rep.setConfig(config);

            Set<Scope> scopes = policy.getScopes();

            if (!scopes.isEmpty()) {
                List<String> scopeNames = scopes.stream().map(Scope::getName).collect(Collectors.toList());
                config.put(SCOPES_CONFIG_KEY, JsonSerialization.writeValueAsString(scopeNames));
            }

            Set<Resource> policyResources = policy.getResources();

            if (!policyResources.isEmpty()) {
                List<String> resourceNames = policyResources.stream().map(Resource::getName).collect(Collectors.toList());
                config.put(RESOURCES_CONFIG_KEY, JsonSerialization.writeValueAsString(resourceNames));
            }

            Set<Policy> associatedPolicies = policy.getAssociatedPolicies();

            if (!associatedPolicies.isEmpty()) {
                config.put(APPLY_POLICIES_CONFIG_KEY, JsonSerialization.writeValueAsString(associatedPolicies.stream().map(associated -> associated.getName()).collect(Collectors.toList())));
            }

            return rep;
        } catch (Exception e) {
            throw new RuntimeException("Error while exporting policy [" + policy.getName() + "].", e);
        }
    }
}
