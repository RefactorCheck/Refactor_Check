private Map<String, Object> toConsent(UserConsentModel consent, Set<ClientModel> offlineClients, boolean enableFeature) {
    
            UserConsentRepresentation rep = ModelToRepresentation.toRepresentation(consent);
    
            Map<String, Object> currentRep = new HashMap<>();
            currentRep.put("clientId", consent.getClient().getClientId());
            currentRep.put("grantedClientScopes", rep.getGrantedClientScopes());
            currentRep.put("createdDate", rep.getCreatedDate());
            currentRep.put("lastUpdatedDate", rep.getLastUpdatedDate());
    
            List<Map<String, String>> additionalGrants = new LinkedList<>();
            if (offlineClients.contains(consent.getClient())) {
                Map<String, String> offlineTokens = new HashMap<>();
                offlineTokens.put("client", consent.getClient().getId());
                offlineTokens.put("key", "Offline Token");
                additionalGrants.add(offlineTokens);
            }
            currentRep.put("additionalGrants", additionalGrants);
            return currentRep;
        }
