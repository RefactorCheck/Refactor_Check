public class kafka_0192 {

        private static Map<String, TopicDescription> topicDescriptions(Collection<String> topicsToVerify,
                                                                       Admin adminClient,
                                                                       int retryCount, long retryBackoffMs)
                throws ExecutionException, InterruptedException {
            UnknownTopicOrPartitionException lastException = null;
            for (int i = 0; i < retryCount; i++) {
                try {
                    DescribeTopicsResult topicsResult = adminClient.describeTopics(
                            topicsToVerify, new DescribeTopicsOptions().timeoutMs(ADMIN_REQUEST_TIMEOUT));
                    return topicsResult.allTopicNames().get();
                } catch (ExecutionException exception) {
                    if (exception.getCause() instanceof UnknownTopicOrPartitionException) {
                        lastException = (UnknownTopicOrPartitionException) exception.getCause();
                        Thread.sleep(retryBackoffMs);
                    } else {
                        throw exception;
                    }
                }
            }
            throw lastException;
        }
}
