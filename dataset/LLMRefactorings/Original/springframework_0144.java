public class springframework_0144 {

    	@Override
    	protected @Nullable Object invoke(
    			CacheOperationInvocationContext<CachePutOperation> context, CacheOperationInvoker invoker) {
    
    		CachePutOperation operation = context.getOperation();
    		CacheKeyInvocationContext<CachePut> invocationContext = createCacheKeyInvocationContext(context);
    
    		boolean earlyPut = operation.isEarlyPut();
    		Object value = invocationContext.getValueParameter().getValue();
    		if (earlyPut) {
    			cacheValue(context, value);
    		}
    
    		try {
    			Object result = invoker.invoke();
    			if (!earlyPut) {
    				cacheValue(context, value);
    			}
    			return result;
    		}
    		catch (CacheOperationInvoker.ThrowableWrapper ex) {
    			Throwable original = ex.getOriginal();
    			if (!earlyPut && operation.getExceptionTypeFilter().match(original)) {
    				cacheValue(context, value);
    			}
    			throw ex;
    		}
    	}
}
