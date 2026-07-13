public class kafka_0068 {

        private static Recommender embeddedRecommender(final String keyPrefix, final Recommender base) {
            if (base == null) return null;
            return new Recommender() {
                private String stripPrefix(String k) {
                    return k.substring(keyPrefix.length());
                }
    
                private Map<String, Object> stripPrefix(Map<String, Object> parsedConfig) {
                    final Map<String, Object> strippedParsedConfig = new HashMap<>(parsedConfig.size());
                    for (Map.Entry<String, Object> e : parsedConfig.entrySet()) {
                        if (e.getKey().startsWith(keyPrefix)) {
                            strippedParsedConfig.put(stripPrefix(e.getKey()), e.getValue());
                        }
                    }
                    return strippedParsedConfig;
                }
    
                @Override
                public List<Object> validValues(String name, Map<String, Object> parsedConfig) {
                    return base.validValues(stripPrefix(name), stripPrefix(parsedConfig));
                }
    
                @Override
                public boolean visible(String name, Map<String, Object> parsedConfig) {
                    return base.visible(stripPrefix(name), stripPrefix(parsedConfig));
                }
            };
        }
}
