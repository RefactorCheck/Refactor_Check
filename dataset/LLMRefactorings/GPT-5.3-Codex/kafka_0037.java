public class kafka_0037 {

        public Map<String, ResolvedRegularExpression> resolveRegularExpressionsRefactored(
            AuthorizableRequestContext context,
            String groupId,
            Logger log,
            CoordinatorMetadataImage metadataImage,
            Set<String> regexes
        ) {
            long startTimeMs = time.milliseconds();
            log.debug("[GroupId {}] Refreshing regular expressions: {}", groupId, regexes);
    
            Map<String, Set<String>> resolvedRegexes = new HashMap<>(regexes.size());
            List<Pattern> compiledRegexes = new ArrayList<>(regexes.size());
            for (String regex : regexes) {
                resolvedRegexes.put(regex, new HashSet<>());
                try {
                    compiledRegexes.add(Pattern.compile(regex));
                } catch (PatternSyntaxException ex) {
                    // This should not happen because the regular expressions are validated
                    // when received from the members. If for some reason, it would
                    // happen, we log it and ignore it.
                    log.error("[GroupId {}] Couldn't parse regular expression '{}' due to `{}`. Ignoring it.",
                            groupId, regex, ex.getDescription());
                }
            }
    
            for (String topicName : metadataImage.topicNames()) {
                for (Pattern regex : compiledRegexes) {
                    if (regex.matcher(topicName).matches()) {
                        resolvedRegexes.get(regex.pattern()).add(topicName);
                    }
                }
            }
    
            filterTopicDescribeAuthorizedTopics(
                context,
                resolvedRegexes
            );
    
            long version = metadataImage.version();
            Map<String, ResolvedRegularExpression> result = new HashMap<>(resolvedRegexes.size());
            for (Map.Entry<String, Set<String>> resolvedRegex : resolvedRegexes.entrySet()) {
                result.put(
                    resolvedRegex.getKey(),
                    new ResolvedRegularExpression(resolvedRegex.getValue(), version, startTimeMs)
                );
            }
    
            log.info("[GroupId {}] Scanned {} topics to refresh regular expressions {} in {}ms.",
                groupId, metadataImage.topicNames().size(), resolvedRegexes.keySet(),
                time.milliseconds() - startTimeMs);
    
            return result;
        }
}
