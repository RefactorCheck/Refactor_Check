public class dubbo_0201 {

    public URLParam addParameters(Map<String, String> parameters) {
        if (CollectionUtils.isEmptyMap(parameters)) {
            return this;
        }

        if (hasAllEqualParameters(parameters)) {
            return this;
        }

        return doAddParameters(parameters, false);
    }

    private boolean hasAllEqualParameters(Map<String, String> parameters) {
        Map<String, String> urlParamMap = getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = urlParamMap.get(entry.getKey());
            if (value == null) {
                if (entry.getValue() != null) {
                    return false;
                }
            } else {
                if (!value.equals(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }
}
