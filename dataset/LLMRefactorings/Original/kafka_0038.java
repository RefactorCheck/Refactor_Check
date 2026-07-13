public class kafka_0038 {

        public void onResponse(
            long currentTimeMs,
            RequestSpec<K> spec,
            AbstractResponse response,
            Node node
        ) {
            clearInflightRequest(currentTimeMs, spec);
    
            if (spec.scope instanceof FulfillmentScope) {
                AdminApiHandler.ApiResult<K, V> result = handler.handleResponse(
                    node,
                    spec.keys,
                    response
                );
                complete(result.completedKeys);
                completeExceptionally(result.failedKeys);
                retryLookup(result.unmappedKeys);
            } else {
                AdminApiLookupStrategy.LookupResult<K> result = handler.lookupStrategy().handleResponse(
                    spec.keys,
                    response
                );
    
                result.completedKeys.forEach(lookupMap::remove);
                completeLookup(result.mappedKeys);
                completeLookupExceptionally(result.failedKeys);
            }
        }
}
