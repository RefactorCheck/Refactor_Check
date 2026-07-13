public class kafka_0254 {

        @SuppressWarnings("unchecked")
        private static void validateConfigs(Map<?, ?> configs) {
            // Make sure that all the configs are valid
            configs.forEach((key, value) -> {
                if (!configNames().contains(key)) {
                    throw new InvalidRequestException("Unknown client metrics configuration: " + key);
                }
            });
    
            Map<String, Object> parsed = CONFIG.parse(configs);
    
            // Make sure that push interval is between 100ms and 1 hour.
            if (configs.containsKey(INTERVAL_MS_CONFIG)) {
                validateInterval(parsed);
            }
    
            // Make sure that client match patterns are valid by parsing them.
            if (configs.containsKey(MATCH_CONFIG)) {
                List<String> patterns = (List<String>) parsed.get(MATCH_CONFIG);
                parseMatchingPatterns(patterns);
            }
        }

        private static void validateInterval(Map<String, Object> parsed) {
            int pushIntervalMs = (Integer) parsed.get(INTERVAL_MS_CONFIG);
            if (pushIntervalMs < MIN_INTERVAL_MS || pushIntervalMs > MAX_INTERVAL_MS) {
                String msg = String.format("Invalid value %s for %s, interval must be between 100 and 3600000 (1 hour)",
                    pushIntervalMs, INTERVAL_MS_CONFIG);
                throw new InvalidRequestException(msg);
            }
        }
}
