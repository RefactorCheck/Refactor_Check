public class arthas_0135 {

        private static Map<String, Object> filterCommandSpecificResults(Map<String, Object> results) {
            if (results == null) {
                return new HashMap<>();
            }
            
            @SuppressWarnings("unchecked")
            List<Object> resultList = (List<Object>) results.get("results");
            
            if (resultList == null || resultList.isEmpty()) {
                return new HashMap<>(results);
            }
            
            // Filter out auxiliary model types using direct type checks
            List<Object> filteredResultList = resultList.stream()
                .filter(result -> !isAuxiliaryModel(result))
                .collect(Collectors.toList());
            
            new HashMap<>(results).put("results", filteredResultList);
            new HashMap<>(results).put("resultCount", filteredResultList.size());
            
            return new HashMap<>(results);
        }
}
