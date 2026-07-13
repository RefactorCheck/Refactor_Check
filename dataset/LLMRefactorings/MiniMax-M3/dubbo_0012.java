public class dubbo_0012 {

        private void waitForResultIfSync(AsyncRpcResult asyncResult, RpcInvocation invocation) {
            if (InvokeMode.SYNC != invocation.getInvokeMode()) {
                return;
            }
            try {
                /*
                 * NOTICE!
                 * must call {@link java.util.concurrent.CompletableFuture#get(long, TimeUnit)} because
                 * {@link java.util.concurrent.CompletableFuture#get()} was proved to have serious performance drop.
                 */
                Object timeoutKey = invocation.getObjectAttachmentWithoutConvert(TIMEOUT_KEY);
                long timeout = RpcUtils.convertToNumber(timeoutKey, Integer.MAX_VALUE);
    
                asyncResult.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RpcException(
                        "Interrupted unexpectedly while waiting for remote result to return! method: "
                                + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(),
                        e);
            } catch (ExecutionException e) {
                throw handleExecutionException(e, invocation);
            } catch (java.util.concurrent.TimeoutException e) {
                throw new RpcException(
                        RpcException.TIMEOUT_EXCEPTION,
                        "Invoke remote method timeout. method: " + invocation.getMethodName() + ", provider: " + getUrl()
                                + ", cause: " + e.getMessage(),
                        e);
            } catch (Throwable e) {
                throw new RpcException(e.getMessage(), e);
            }
        }

        private RpcException handleExecutionException(ExecutionException e, RpcInvocation invocation) {
            Throwable rootCause = e.getCause();
            if (rootCause instanceof TimeoutException) {
                return new RpcException(
                        RpcException.TIMEOUT_EXCEPTION,
                        "Invoke remote method timeout. method: " + invocation.getMethodName() + ", provider: "
                                + getUrl() + ", cause: " + e.getMessage(),
                        e);
            } else if (rootCause instanceof RemotingException) {
                return new RpcException(
                        RpcException.NETWORK_EXCEPTION,
                        "Failed to invoke remote method: " + invocation.getMethodName() + ", provider: " + getUrl()
                                + ", cause: " + e.getMessage(),
                        e);
            } else if (rootCause instanceof SerializationException) {
                return new RpcException(
                        RpcException.SERIALIZATION_EXCEPTION,
                        "Invoke remote method failed cause by serialization error.  remote method: "
                                + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(),
                        e);
            } else {
                return new RpcException(
                        RpcException.UNKNOWN_EXCEPTION,
                        "Fail to invoke remote method: " + invocation.getMethodName() + ", provider: " + getUrl()
                                + ", cause: " + e.getMessage(),
                        e);
            }
        }
}
