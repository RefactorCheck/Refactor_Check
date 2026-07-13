public class keycloak_0244 {

        private Map<String, Object> toConsent(UserConsentModel consent, Set<ClientModel> offlineClients) {

            UserConsentRepresentation rep = ModelToRepresentation.toRepresentation(consent);

            Map<String, Object> currentRep = new HashMap<>();
            currentRep.put("clientId", consent.getClient().getClientId());
            currentRep.put("grantedClientScopes", rep.getGrantedClientScopes());
            currentRep.put("createdDate", rep.getCreatedDate());
            currentRep.put("lastUpdatedDate", rep.getLastUpdatedDate());

            List<Map<String, String>> additionalGrants = new LinkedList<>();
            if (offlineClients.contains(consent.getClient())) {
                additionalGrants.add(buildOfflineTokensEntry(consent));
            }
            currentRep.put("additionalGrants", additionalGrants);
            return currentRep;
        }

        private Map<String, String> buildOfflineTokensEntry(UserConsentModel consent) {
            Map<String, String> offlineTokens = new HashMap<>();
            offlineTokens.put("client", consent.getClient().getId());
            offlineTokens.put("key", "Offline Token");
            return offlineTokens;
        }
}
