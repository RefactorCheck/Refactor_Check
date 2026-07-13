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
    
            // Combine filters and listeners on Provider and Consumer
            String remoteValue = super.getParameter(key);
            String localValue = consumerURL.getParameter(key);
            if (remoteValue != null && remoteValue.length() > 0 && localValue != null && localValue.length() > 0) {
                value = remoteValue + "," + localValue;
                concatenatedPrams.put(key, value);
                return value;
            }
            if (localValue != null && localValue.length() > 0) {
                value = localValue;
            } else if (remoteValue != null && remoteValue.length() > 0) {
                value = remoteValue;
            }
            concatenatedPrams.put(key, value);
            return value;
        }
}
