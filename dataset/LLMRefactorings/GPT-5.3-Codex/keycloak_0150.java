@Override
        public void setClaim(Map<String, Object> claims, UserSessionModel userSessionModel) {
            String userAttribute = mapperModel.getConfig().get(USER_ATTRIBUTE_KEY);
            if ((mapperModel.getConfig().get(CLAIM_NAME)) == null && userAttribute == null) {
                return;
            }
            boolean aggregateAttributes = Optional.ofNullable(mapperModel.getConfig().get(AGGREGATE_ATTRIBUTES_KEY))
                    .map(Boolean::parseBoolean).orElse(false);
            Collection<String> attributes =
                    KeycloakModelUtils.resolveAttribute(userSessionModel.getUser(), userAttribute,
                            aggregateAttributes);
            attributes.removeAll(Collections.singleton(null));
            if (!attributes.isEmpty()) {
                JsonUtils.mapClaim(
                        JsonUtils.splitClaimPath(Optional.ofNullable((mapperModel.getConfig().get(CLAIM_NAME))).orElse(userAttribute)),
                        String.join(",", attributes),
                        claims,
                        false
                );
            }
        }
