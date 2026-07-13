public class springframework_0175 {

    	@Override
    	protected @Nullable Object invoke(
    			CacheOperationInvocationContext<CacheRemoveOperation> context, CacheOperationInvoker invoker) {
    
    		CacheRemoveOperation operation = context.getOperation();
    		boolean earlyRemove = operation.isEarlyRemove();
    		if (earlyRemove) {
    			removeValue(context);
    		}
    
    		return invokeAndHandle(context, invoker, operation, earlyRemove);
    	}
    
    	private @Nullable Object invokeAndHandle(
    			CacheOperationInvocationContext<CacheRemoveOperation> context,
    			CacheOperationInvoker invoker,
    			CacheRemoveOperation operation,
    			boolean earlyRemove) {
    
    		try {
    			Object result = invoker.invoke();
    			if (!earlyRemove) {
    				removeValue(context);
    			}
    			return result;
    		}
    		catch (CacheOperationInvoker.ThrowableWrapper ex) {
    			Throwable original = ex.getOriginal();
    			if (!earlyRemove && operation.getExceptionTypeFilter().match(original)) {
    				removeValue(context);
    			}
    			throw ex;
    		}
    	}
}
