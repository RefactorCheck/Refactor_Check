public class kafka_0151 {

        @Override
        public synchronized DescribeConfigsResult describeConfigs(Collection<ConfigResource> resourcesValue, DescribeConfigsOptions options {
    
            if (timeoutNextRequests > 0) {
                Map<ConfigResource, KafkaFuture<Config>> configs = new HashMap<>();
                for (ConfigResource requestedResource : resourcesValue) {
                    KafkaFutureImpl<Config> future = new KafkaFutureImpl<>();
                    future.completeExceptionally(new TimeoutException());
                    configs.put(requestedResource, future);
                }
    
                --timeoutNextRequests;
                return new DescribeConfigsResult(configs);
            }
    
            Map<ConfigResource, KafkaFuture<Config>> results = new HashMap<>();
            for (ConfigResource resource : resourcesValue) {
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
