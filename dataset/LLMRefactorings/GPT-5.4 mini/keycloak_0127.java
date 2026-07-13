public class keycloak_0127 {

        public static String mapLoaToAcr(int loa, Map<String, Integer> acrLoaMap, Collection<String> acrValues) {
            String acr = null;
            if (!acrLoaMap.isEmpty() && !acrValues.isEmpty()) {
                int maxLoa = -1;
                for (String acrValue : acrValues) {
                    Integer mappedLoa = resolveMappedLoa(acrLoaMap, acrValue);
                    if (mappedLoa != null && mappedLoa > maxLoa && loa >= mappedLoa) {
                        acr = acrValue;
                        maxLoa = mappedLoa;
                    }
                }
            }
            return acr;
        }

        private static Integer resolveMappedLoa(Map<String, Integer> acrLoaMap, String acrValue) {
            Integer mappedLoa = acrLoaMap.get(acrValue);
            if (mappedLoa == null) {
                try {
                    mappedLoa = Integer.parseInt(acrValue);
                } catch (NumberFormatException e) {
                    LOGGER.warnf("Acr value '%s' cannot be mapped to int", acrValue);
                }
            }
            return mappedLoa;
        }
}
