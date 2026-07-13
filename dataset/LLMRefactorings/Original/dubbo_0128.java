public class dubbo_0128 {

        private void extractInstanceParams(URL url, List<MetadataParamsFilter> filters) {
            if (CollectionUtils.isEmpty(filters)) {
                return;
            }
    
            String[] included, excluded;
            if (filters.size() == 1) {
                MetadataParamsFilter filter = filters.get(0);
                included = filter.instanceParamsIncluded();
                excluded = filter.instanceParamsExcluded();
            } else {
                Set<String> includedList = new HashSet<>();
                Set<String> excludedList = new HashSet<>();
                filters.forEach(filter -> {
                    if (ArrayUtils.isNotEmpty(filter.instanceParamsIncluded())) {
                        includedList.addAll(Arrays.asList(filter.instanceParamsIncluded()));
                    }
                    if (ArrayUtils.isNotEmpty(filter.instanceParamsExcluded())) {
                        excludedList.addAll(Arrays.asList(filter.instanceParamsExcluded()));
                    }
                });
                included = includedList.toArray(new String[0]);
                excluded = excludedList.toArray(new String[0]);
            }
    
            Map<String, String> tmpInstanceParams = new HashMap<>();
            if (ArrayUtils.isNotEmpty(included)) {
                for (String p : included) {
                    String value = url.getParameter(p);
                    if (value != null) {
                        tmpInstanceParams.put(p, value);
                    }
                }
            } else if (ArrayUtils.isNotEmpty(excluded)) {
                tmpInstanceParams.putAll(url.getParameters());
                for (String p : excluded) {
                    tmpInstanceParams.remove(p);
                }
            }
    
            tmpInstanceParams.forEach((key, value) -> {
                String oldValue = instanceParams.put(key, value);
                if (!TIMESTAMP_KEY.equals(key) && oldValue != null && !oldValue.equals(value)) {
                    throw new IllegalStateException(String.format(
                            "Inconsistent instance metadata found in different services: %s, %s", oldValue, value));
                }
            });
        }
}
