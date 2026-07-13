public class kafka_0151 {

        @Override
        public synchronized DescribeConfigsResult describeConfigs(Collection<ConfigResource> resources, DescribeConfigsOptions options) {
    
            final boolean shouldTimeout = timeoutNextRequests > 0;
            if (shouldTimeout) {
                Map<ConfigResource, KafkaFuture<Config>> configs = new HashMap<>();
                for (ConfigResource requestedResource : resources) {
                    KafkaFutureImpl<Config> future = new KafkaFutureImpl<>();
                    future.completeExceptionally(new TimeoutException());
                    configs.put(requestedResource, future);
                }
    
                --timeoutNextRequests;
                return new DescribeConfigsResult(configs);
            }
    
            Map<ConfigResource, KafkaFuture<Config>> results = new HashMap<>();
            for (ConfigResource resource : resources) {
                KafkaFutureImpl<Config> future = new KafkaFutureImpl<>();
                results.put(resource, future);
                try {
                    future.complete(getResourceDescription(resource));
                } catch (Throwable e) {
                    future.completeExceptionally(e);
                }
            }
            return new DescribeConfigsResult(results);
        }
}
