public class kafka_0024 {

        public static void validate(Map<String, String> props, Map<String, ConfigValue> validatedConfig) {
            final String topicsList = props.get(TOPICS_CONFIG);
            final String topicsRegex = props.get(TOPICS_REGEX_CONFIG);
            final String dlqTopic = props.getOrDefault(DLQ_TOPIC_NAME_CONFIG, "").trim();
            final boolean hasTopicsConfig = !Utils.isBlank(topicsList);
            final boolean hasTopicsRegexConfig = !Utils.isBlank(topicsRegex);
            final boolean hasDlqTopicConfig = !Utils.isBlank(dlqTopic);
    
            if (hasTopicsConfig && hasTopicsRegexConfig) {
                String errorMessage = TOPICS_CONFIG + " and " + TOPICS_REGEX_CONFIG + " are mutually exclusive options, but both are set.";
                addErrorMessage(validatedConfig, TOPICS_CONFIG, topicsList, errorMessage);
                addErrorMessage(validatedConfig, TOPICS_REGEX_CONFIG, topicsRegex, errorMessage);
            }
    
            if (!hasTopicsConfig && !hasTopicsRegexConfig) {
                String errorMessage = "Must configure one of " + TOPICS_CONFIG + " or " + TOPICS_REGEX_CONFIG;
                addErrorMessage(validatedConfig, TOPICS_CONFIG, topicsList, errorMessage);
                addErrorMessage(validatedConfig, TOPICS_REGEX_CONFIG, topicsRegex, errorMessage);
            }
    
            if (hasDlqTopicConfig) {
                validateDlqTopicConfiguration(props, dlqTopic, topicsList, topicsRegex, validatedConfig);
            }
        }

        private static void validateDlqTopicConfiguration(Map<String, String> props, String dlqTopic, String topicsList, String topicsRegex, Map<String, ConfigValue> validatedConfig) {
            if (!Utils.isBlank(topicsList)) {
                List<String> topics = parseTopicsList(props);
                if (topics.contains(dlqTopic)) {
                    String errorMessage = String.format(
                            "The DLQ topic '%s' may not be included in the list of topics ('%s=%s') consumed by the connector",
                            dlqTopic, TOPICS_CONFIG, topics
                    );
                    addErrorMessage(validatedConfig, TOPICS_CONFIG, topicsList, errorMessage);
                }
            }
            if (!Utils.isBlank(topicsRegex)) {
                Pattern pattern = Pattern.compile(topicsRegex);
                if (pattern.matcher(dlqTopic).matches()) {
                    String errorMessage = String.format(
                            "The DLQ topic '%s' may not be matched by the regex for the topics ('%s=%s') consumed by the connector",
                            dlqTopic, TOPICS_REGEX_CONFIG, topicsRegex
                    );
                    addErrorMessage(validatedConfig, TOPICS_REGEX_CONFIG, topicsRegex, errorMessage);
                }
            }
        }
}
