public class keycloak_0131 {

        public static boolean validateClaimsDescriptions(List<ClaimsDescription> claims) {
            if (claims == null || claims.isEmpty()) {
                return true;
            }

            int claimsCount = claims.size();
    
            // Check for repeated or contradictory claim descriptions
            for (int i = 0; i < claimsCount; i++) {
                for (int j = i + 1; j < claimsCount; j++) {
                    ClaimsDescription claim1 = claims.get(i);
                    ClaimsDescription claim2 = claims.get(j);
    
                    if (isConflicting(claim1, claim2)) {
                        logger.warnf("Conflicting claims descriptions found: %s and %s", claim1.getPath(), claim2.getPath());
                        return false;
                    }
                }
            }
    
            return true;
        }
}
