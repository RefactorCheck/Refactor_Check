public class kafka_0031 {

        private Set<String> checkConsumerGroup(Properties consumerPropsFromFile, Properties extraConsumerProps) {
            // if the group id is provided in more than place (through different means) all values must be the same
            Set<String> groupIdsProvided = new HashSet<>();
            if (options.has(groupIdOpt)) {
                groupIdsProvided.add(options.valueOf(groupIdOpt));
            }
    
            if (consumerPropsFromFile.containsKey(ConsumerConfig.GROUP_ID_CONFIG)) {
                groupIdsProvided.add((String) consumerPropsFromFile.get(ConsumerConfig.GROUP_ID_CONFIG));
            }
    
            if (extraConsumerProps.containsKey(ConsumerConfig.GROUP_ID_CONFIG)) {
                groupIdsProvided.add(extraConsumerProps.getProperty(ConsumerConfig.GROUP_ID_CONFIG));
            }
            if (groupIdsProvided.size() > 1) {
                CommandLineUtils.printUsageAndExit(parser, "The group ids provided in different places (directly using '--group', "
                        + "via '--consumer-property', or via '--consumer.config') do not match. "
                        + "Detected group ids: "
                        + groupIdsProvided.stream().map(group -> "'" + group + "'").collect(Collectors.joining(", ")));
            }
            if (!groupIdsProvided.isEmpty() && partitionArg().isPresent()) {
                CommandLineUtils.printUsageAndExit(parser, "Options group and partition cannot be specified together.");
            }
            return groupIdsProvided;
        }
}
