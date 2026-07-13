public class springframework_0088 {

    	@SuppressWarnings("unchecked")
    	private @Nullable Object execute(CacheOperationInvocationContext<?> context, CacheOperationInvoker invoker) {
    		CacheOperationInvoker adapter = new CacheOperationInvokerAdapter(invoker);
    		BasicOperation operation = context.getOperation();
    
    		if (operation instanceof CacheResultOperation) {
    			Assert.state(this.cacheResultInterceptor != null, "No CacheResultInterceptor");
    			return this.cacheResultInterceptor.invoke(
    					(CacheOperationInvocationContext<CacheResultOperation>) context, adapter);
    		}
    		else if (operation instanceof CachePutOperation) {
    			Assert.state(this.cachePutInterceptor != null, "No CachePutInterceptor");
    			return this.cachePutInterceptor.invoke(
    					(CacheOperationInvocationContext<CachePutOperation>) context, adapter);
    		}
    		else if (operation instanceof CacheRemoveOperation) {
    			Assert.state(this.cacheRemoveEntryInterceptor != null, "No CacheRemoveEntryInterceptor");
    			return this.cacheRemoveEntryInterceptor.invoke(
    					(CacheOperationInvocationContext<CacheRemoveOperation>) context, adapter);
    		}
    		else if (operation instanceof CacheRemoveAllOperation) {
    			Assert.state(this.cacheRemoveAllInterceptor != null, "No CacheRemoveAllInterceptor");
    			return this.cacheRemoveAllInterceptor.invoke(
    					(CacheOperationInvocationContext<CacheRemoveAllOperation>) context, adapter);
    		}
    		else {
    			throw new IllegalArgumentException("Cannot handle " + operation);
    		}
    	}
}
