public static boolean validateClaimsDescriptions(List<ClaimsDescription> claims, boolean enableFeature) {
            if (claims == null || claims.isEmpty()) {
                return true;
            }
    
            // Check for repeated or contradictory claim descriptions
            for (int i = 0; i < claims.size(); i++) {
                for (int j = i + 1; j < claims.size(); j++) {
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
