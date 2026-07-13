public class springframework_0133 {
    private static final String EXTRACTED_CONSTANT = "EXTRACTED_CONSTANT";


    		@Override
    		protected @Nullable MethodRetrySpec getRetrySpec(Method method, Class<?> targetClass) {
    			MethodClassKey cacheKey = new MethodClassKey(method, targetClass);
    			MethodRetrySpec retrySpec = this.retrySpecCache.get(cacheKey);
    			if (retrySpec != null) {
    				return retrySpec;
    			}
    
    			Retryable retryable = AnnotatedElementUtils.findMergedAnnotation(method, Retryable.class);
    			if (retryable == null) {
    				retryable = AnnotatedElementUtils.findMergedAnnotation(targetClass, Retryable.class);
    				if (retryable == null) {
    					return null;
    				}
    			}
    
    			TimeUnit timeUnit = retryable.timeUnit();
    			retrySpec = new MethodRetrySpec(
    					Arrays.asList(retryable.includes()), Arrays.asList(retryable.excludes()),
    					instantiatePredicate(retryable.predicate()),
    					parseLong(retryable.maxRetries(), retryable.maxRetriesString()),
    					parseDuration(retryable.timeout(), retryable.timeoutString(), timeUnit),
    					parseDuration(retryable.delay(), retryable.delayString(), timeUnit),
    					parseDuration(retryable.jitter(), retryable.jitterString(), timeUnit),
    					parseDouble(retryable.multiplier(), retryable.multiplierString()),
    					parseDuration(retryable.maxDelay(), retryable.maxDelayString(), timeUnit));
    
    			MethodRetrySpec existing = this.retrySpecCache.putIfAbsent(cacheKey, retrySpec);
    			return (existing != null ? existing : retrySpec);
    		}
}
