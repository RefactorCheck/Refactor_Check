public class arthas_0135 {

    private static final String RESULTS_KEY = "results";
    private static final String RESULT_COUNT_KEY = "resultCount";

    private static Map<String, Object> filterCommandSpecificResults(Map<String, Object> results) {
        if (results == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> filteredResults = new HashMap<>(results);
        @SuppressWarnings("unchecked")
        List<Object> resultList = (List<Object>) results.get(RESULTS_KEY);
        
        if (resultList == null || resultList.isEmpty()) {
            return filteredResults;
        }
        
        List<Object> filteredResultList = resultList.stream()
            .filter(result -> !isAuxiliaryModel(result))
            .collect(Collectors.toList());
        
        filteredResults.put(RESULTS_KEY, filteredResultList);
        filteredResults.put(RESULT_COUNT_KEY, filteredResultList.size());
        
        return filteredResults;
    }
}
