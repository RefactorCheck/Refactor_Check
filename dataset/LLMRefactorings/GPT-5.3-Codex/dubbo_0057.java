public class dubbo_0057 {

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Result doMockInvokeRefactored(Invocation invocation, RpcException e) {
            Result result;
            Invoker<T> mockInvoker;
    
            RpcInvocation rpcInvocation = (RpcInvocation) invocation;
            rpcInvocation.setInvokeMode(RpcUtils.getInvokeMode(getUrl(), invocation));
    
            List<Invoker<T>> mockInvokers = selectMockInvoker(invocation);
            if (CollectionUtils.isEmpty(mockInvokers)) {
                mockInvoker = (Invoker<T>) new MockInvoker(getUrl(), directory.getInterface());
            } else {
                mockInvoker = mockInvokers.get(0);
            }
            try {
                result = mockInvoker.invoke(invocation);
            } catch (RpcException mockException) {
                if (mockException.isBiz()) {
                    result = AsyncRpcResult.newDefaultAsyncResult(mockException.getCause(), invocation);
                } else {
                    throw new RpcException(
                            mockException.getCode(), getMockExceptionMessage(e, mockException), mockException.getCause());
                }
            } catch (Throwable me) {
                throw new RpcException(getMockExceptionMessage(e, me), me.getCause());
            }
            if (setFutureWhenSync || rpcInvocation.getInvokeMode() != InvokeMode.SYNC) {
                // set server context
                RpcContext.getServiceContext()
                        .setFuture(new FutureAdapter<>(((AsyncRpcResult) result).getResponseFuture()));
            }
            return result;
        }
}
