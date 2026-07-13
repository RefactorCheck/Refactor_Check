public class kafka_0068 {

        private static Recommender embeddedRecommender(final String keyPrefix, final Recommender base) {
            if (base == null) return null;
            return new Recommender() {
                private String unprefixed(String k) {
                    return k.substring(keyPrefix.length());
                }
    
                private Map<String, Object> unprefixed(Map<String, Object> parsedConfig) {
                    final Map<String, Object> unprefixedParsedConfig = new HashMap<>(parsedConfig.size());
                    for (Map.Entry<String, Object> e : parsedConfig.entrySet()) {
                        if (e.getKey().startsWith(keyPrefix)) {
                            unprefixedParsedConfig.put(unprefixed(e.getKey()), e.getValue());
                        }
                    }
                    return unprefixedParsedConfig;
                }
    
                @Override
                public List<Object> validValues(String name, Map<String, Object> parsedConfig) {
                    return base.validValues(unprefixed(name), unprefixed(parsedConfig));
                }
    
                @Override
                public boolean visible(String name, Map<String, Object> parsedConfig) {
                    return base.visible(unprefixed(name), unprefixed(parsedConfig));
                }
            };
        }
}
