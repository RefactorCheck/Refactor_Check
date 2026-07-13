public class dubbo_0057 {

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Result doMockInvoke(Invocation invocation, RpcException e) {
            Result result;

            RpcInvocation rpcInvocation = (RpcInvocation) invocation;
            rpcInvocation.setInvokeMode(RpcUtils.getInvokeMode(getUrl(), invocation));

            Invoker<T> mockInvoker = getMockInvoker(invocation);
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
                RpcContext.getServiceContext()
                        .setFuture(new FutureAdapter<>(((AsyncRpcResult) result).getResponseFuture()));
            }
            return result;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Invoker<T> getMockInvoker(Invocation invocation) {
            List<Invoker<T>> mockInvokers = selectMockInvoker(invocation);
            if (CollectionUtils.isEmpty(mockInvokers)) {
                return (Invoker<T>) new MockInvoker(getUrl(), directory.getInterface());
            } else {
                return mockInvokers.get(0);
            }
        }
}
