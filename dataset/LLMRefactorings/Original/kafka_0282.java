public class kafka_0282 {

        public static Optional<StaticAssertionJwtTemplate> staticAssertionJwtTemplate(ConfigurationUtils cu) {
            if (cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_AUD) ||
                cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_ISS) ||
                cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_SUB)) {
                Map<String, Object> staticClaimsPayload = new HashMap<>();
    
                if (cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_AUD))
                    staticClaimsPayload.put("aud", cu.validateString(SASL_OAUTHBEARER_ASSERTION_CLAIM_AUD));
    
                if (cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_ISS))
                    staticClaimsPayload.put("iss", cu.validateString(SASL_OAUTHBEARER_ASSERTION_CLAIM_ISS));
    
                if (cu.containsKey(SASL_OAUTHBEARER_ASSERTION_CLAIM_SUB))
                    staticClaimsPayload.put("sub", cu.validateString(SASL_OAUTHBEARER_ASSERTION_CLAIM_SUB));
    
                Map<String, Object> header = Map.of();
                return Optional.of(new StaticAssertionJwtTemplate(header, staticClaimsPayload));
            } else {
                return Optional.empty();
            }
        }
}
