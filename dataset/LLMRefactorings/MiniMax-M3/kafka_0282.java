public class kafka_0282 {

        public static Optional<StaticAssertionJwtTemplate> staticAssertionJwtTemplate(ConfigurationUtils cu) {
            if (cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_AUD) ||
                cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_ISS) ||
                cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_SUB)) {
                Map<String, Object> staticClaimsPayload = new HashMap<>();
                addClaimIfPresent(cu, staticClaimsPayload, "aud", SASL_OAUTHBEARER_ASSERTION_CLAIM_AUD);
                addClaimIfPresent(cu, staticClaimsPayload, "iss", SASL_OAUTHBEARER_ASSERTION_CLAIM_ISS);
                addClaimIfPresent(cu, staticClaimsPayload, "sub", SASL_OAUTHBEARER_ASSERTION_CLAIM_SUB);
                Map<String, Object> header = Map.of();
                return Optional.of(new StaticAssertionJwtTemplate(header, staticClaimsPayload));
            } else {
                return Optional.empty();
            }
        }
        
        private static void addClaimIfPresent(ConfigurationUtils cu, Map<String, Object> payload, String claimName, String configKey) {
            if (cu.containsKey(configKey))
                payload.put(claimName, cu.validateString(configKey));
        }
}
