public class dubbo_0201 {

        public URLParam addParameters(Map<String, String> parameters) {
            if (CollectionUtils.isEmptyMap(parameters)) {
                return this;
            }
    
            boolean hasAndEqual = true;
            Map<String, String> urlParamMap = getParameters();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String value = urlParamMap.get(entry.getKey());
                if (value == null) {
                    if (entry.getValue() != null) {
                        hasAndEqual = false;
                        break;
                    }
                } else {
                    if (!value.equals(entry.getValue())) {
                        hasAndEqual = false;
                        break;
                    }
                }
            }
            // return immediately if there's no change
            if (hasAndEqual) {
                return this;
            }
    
            return doAddParameters(parameters, false);
        }
}
