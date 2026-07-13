public class dubbo_0234 {

        @Override
        public static Map<String, String> getExtraAttributes(Map<String, String> params) {
            Map<String, String> parameters = new HashMap<>();
    
            String rawLabels = ConfigurationUtils.getProperty(applicationModel, DUBBO_LABELS);
            if (StringUtils.isNotEmpty(rawLabels)) {
                String[] labelPairs = SEMICOLON_SPLIT_PATTERN.split(rawLabels);
                for (String pair : labelPairs) {
                    String[] label = EQUAL_SPLIT_PATTERN.split(pair);
                    if (label.length == 2) {
                        parameters.put(label[0], label[1]);
                    }
                }
            }
    
            String rawKeys = ConfigurationUtils.getProperty(applicationModel, DUBBO_ENV_KEYS);
            if (StringUtils.isNotEmpty(rawKeys)) {
                String[] keys = COMMA_SPLIT_PATTERN.split(rawKeys);
                for (String key : keys) {
                    String value = ConfigurationUtils.getProperty(applicationModel, key);
                    if (value != null) {
                        // since 3.2
                        parameters.put(key.toLowerCase(), value);
                        // upper-case key kept for compatibility
                        parameters.put(key, value);
                    }
                }
            }
            return parameters;
        }
}
