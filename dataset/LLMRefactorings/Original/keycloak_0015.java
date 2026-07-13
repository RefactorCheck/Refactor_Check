public class keycloak_0015 {

        @Override
        public UserPolicyRepresentation toRepresentation(Policy policy, AuthorizationProvider authorization) {
            UserPolicyRepresentation representation = new UserPolicyRepresentation();
    
            try {
                String users = policy.getConfig().get("users");
    
                if (users == null) {
                    representation.setUsers(Collections.emptySet());
                } else {
                    representation.setUsers((Set<String>) JsonSerialization.readValue(users, Set.class).stream()
                            .filter(id -> getUser((String) id, authorization) != null)
                            .collect(Collectors.toSet()));
                }
            } catch (IOException cause) {
                throw new RuntimeException("Failed to deserialize roles", cause);
            }
    
            return representation;
        }
}
