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
            cacheValueOnException(ex, operation, earlyPut, context, value);
            throw ex;
        }
    }

    private void cacheValueOnException(CacheOperationInvoker.ThrowableWrapper ex, CachePutOperation operation,
            boolean earlyPut, CacheOperationInvocationContext<CachePutOperation> context, Object value) {
        Throwable original = ex.getOriginal();
        if (!earlyPut && operation.getExceptionTypeFilter().match(original)) {
            cacheValue(context, value);
        }
    }
}
