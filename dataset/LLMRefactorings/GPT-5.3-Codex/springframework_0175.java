public class springframework_0175 {
	private CacheRemoveOperation operation;


    	@Override
    	protected @Nullable Object invoke(
    			CacheOperationInvocationContext<CacheRemoveOperation> context, CacheOperationInvoker invoker) {
    
    		operation = context.getOperation();
    		boolean earlyRemove = operation.isEarlyRemove();
    		if (earlyRemove) {
    			removeValue(context);
    		}
    
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
