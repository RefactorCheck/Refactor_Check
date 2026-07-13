public class dubbo_0134 {

        @Override
        public String getConcatenatedParameter(String key) {
            if (concatenatedPrams == null) {
                concatenatedPrams = new HashMap<>(1);
            }
            String value = concatenatedPrams.get(key);
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
    
            value = concatenateParameterValues(key);
            concatenatedPrams.put(key, value);
            return value;
        }
    
        private String concatenateParameterValues(String key) {
            String remoteValue = super.getParameter(key);
            String localValue = consumerURL.getParameter(key);
            if (remoteValue != null && remoteValue.length() > 0 && localValue != null && localValue.length() > 0) {
                return remoteValue + "," + localValue;
            }
            if (localValue != null && localValue.length() > 0) {
                return localValue;
            }
            return remoteValue;
        }
}
