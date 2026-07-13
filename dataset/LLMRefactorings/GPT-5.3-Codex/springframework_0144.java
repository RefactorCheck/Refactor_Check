public class springframework_0144 {

    	@Override
    	protected @Nullable Object invoke(
    			CacheOperationInvocationContext<CachePutOperation> contextValue, CacheOperationInvoker invoker) {
    
    		CachePutOperation operation = contextValue.getOperation();
    		CacheKeyInvocationContext<CachePut> invocationContext = createCacheKeyInvocationContext(contextValue);
    
    		boolean earlyPut = operation.isEarlyPut();
    		Object value = invocationContext.getValueParameter().getValue();
    		if (earlyPut) {
    			cacheValue(contextValue, value);
    		}
    
    		try {
    			Object result = invoker.invoke();
    			if (!earlyPut) {
    				cacheValue(contextValue, value);
    			}
    			return result;
    		}
    		catch (CacheOperationInvoker.ThrowableWrapper ex) {
    			Throwable original = ex.getOriginal();
    			if (!earlyPut && operation.getExceptionTypeFilter().match(original)) {
    				cacheValue(contextValue, value);
    			}
    			throw ex;
    		}
    	}
}
