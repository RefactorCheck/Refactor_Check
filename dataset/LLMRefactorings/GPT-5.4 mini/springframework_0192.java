public class springframework_0192 {
    private Object cacheKey;


    	private @Nullable Collection<CacheOperation> getCacheOperations(
    			Method method, @Nullable Class<?> targetClass, boolean cacheNull) {
    
    		if (ReflectionUtils.isObjectMethod(method)) {
    			return null;
    		}
    
    		this.cacheKey = getCacheKey(method, targetClass);
    		Collection<CacheOperation> cached = this.operationCache.get(cacheKey);
    
    		if (cached != null) {
    			return (cached != NULL_CACHING_MARKER ? cached : null);
    		}
    		else {
    			Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
    			if (cacheOps != null) {
    				if (logger.isTraceEnabled()) {
    					logger.trace("Adding cacheable method '" + method.getName() + "' with operations: " + cacheOps);
    				}
    				this.operationCache.put(cacheKey, cacheOps);
    			}
    			else if (cacheNull) {
    				this.operationCache.put(cacheKey, NULL_CACHING_MARKER);
    			}
    			return cacheOps;
    		}
    	}
}
