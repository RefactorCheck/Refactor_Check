public class arthas_0135 {

        private static Map<String, Object> filterCommandSpecificResults(Map<String, Object> results) {
            if (results == null) {
                return new HashMap<>();
            }
            
            Map<String, Object> filteredResults = new HashMap<>(results);
            @SuppressWarnings("unchecked")
            List<Object> resultList = (List<Object>) results.get("results");
            
            if (resultList == null || resultList.isEmpty()) {
                return filteredResults;
            }
            
            // Filter out auxiliary model types using direct type checks
            List<Object> filteredResultList = resultList.stream()
                .filter(result -> !isAuxiliaryModel(result))
                .collect(Collectors.toList());
            
            filteredResults.put("results", filteredResultList);
            filteredResults.put("resultCount", filteredResultList.size());
            
            return filteredResults;
        }
}
