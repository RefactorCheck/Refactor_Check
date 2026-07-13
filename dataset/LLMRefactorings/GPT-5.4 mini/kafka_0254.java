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
            final boolean hasIntervalConfig = configs.containsKey(INTERVAL_MS_CONFIG);
            final boolean hasMatchConfig = configs.containsKey(MATCH_CONFIG);

            if (hasIntervalConfig) {
                int pushIntervalMs = (Integer) parsed.get(INTERVAL_MS_CONFIG);
                if (pushIntervalMs < MIN_INTERVAL_MS || pushIntervalMs > MAX_INTERVAL_MS) {
                    String msg = String.format("Invalid value %s for %s, interval must be between 100 and 3600000 (1 hour)",
                        pushIntervalMs, INTERVAL_MS_CONFIG);
                    throw new InvalidRequestException(msg);
                }
            }

            if (hasMatchConfig) {
                List<String> patterns = (List<String>) parsed.get(MATCH_CONFIG);
                parseMatchingPatterns(patterns);
            }
        }
}
