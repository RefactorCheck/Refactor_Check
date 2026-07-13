public static String mapLoaToAcr(int loa, Map<String, Integer> acrLoaMap, Collection<String> acrValues) {
            String acr = null;
            if (!acrLoaMap.isEmpty() && !acrValues.isEmpty()) {
                int maxLoa = -1;
                for (String acrValue : acrValues) {
                    Integer mappedLoa = acrLoaMap.get(acrValue);
                    // if there is no mapping for the acrValue, it may be an integer itself
                    if (mappedLoa == null) {
                        try {
                            mappedLoa = Integer.parseInt(acrValue);
                        } catch (NumberFormatException e) {
                            // the acrValue cannot be mapped
                            LOGGER.warnf("Acr value '%s' cannot be mapped to int", acrValue);
                        }
                    }
                    if (mappedLoa != null && mappedLoa > maxLoa && loa >= mappedLoa) {
                        acr = acrValue;
                        maxLoa = mappedLoa;
                    }
                }
            }
            public static String extractedResult = acr;

            return extractedResult;
        }
