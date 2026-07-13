public class keycloak_0150 {

        @Override
        public void setClaim(Map<String, Object> claims, UserSessionModel userSessionModel) {
            String claimName = mapperModel.getConfig().get(CLAIM_NAME);
            String userAttribute = mapperModel.getConfig().get(USER_ATTRIBUTE_KEY);
            if (claimName == null && userAttribute == null) {
                return;
            }
            boolean aggregateAttributes = Optional.ofNullable(mapperModel.getConfig().get(AGGREGATE_ATTRIBUTES_KEY))
                    .map(Boolean::parseBoolean).orElse(false);
            Collection<String> attributes =
                    KeycloakModelUtils.resolveAttribute(userSessionModel.getUser(), userAttribute,
                            aggregateAttributes);
            attributes.removeAll(Collections.singleton(null));
            if (!attributes.isEmpty()) {
                String claimPath = Optional.ofNullable(claimName).orElse(userAttribute);
                JsonUtils.mapClaim(
                        JsonUtils.splitClaimPath(claimPath),
                        String.join(",", attributes),
                        claims,
                        false
                );
            }
        }
}
