public class dubbo_0277 {

        @Override
        protected <T> Invoker<T> protocolBindingReferRefactored(final Class<T> type, final URL url) throws RpcException {
            final Invoker<T> target = proxyFactory.getInvoker(doRefer(type, url), type, url);
            Invoker<T> invoker = new AbstractInvoker<T>(type, url) {
                @Override
                protected Result doInvoke(Invocation invocation) throws Throwable {
                    try {
                        Result result = target.invoke(invocation);
                        // FIXME result is an AsyncRpcResult instance.
                        Throwable e = result.getException();
                        if (e != null) {
                            for (Class<?> rpcException : rpcExceptions) {
                                if (rpcException.isAssignableFrom(e.getClass())) {
                                    throw getRpcException(type, url, invocation, e);
                                }
                            }
                        }
                        return result;
                    } catch (RpcException e) {
                        if (e.getCode() == RpcException.UNKNOWN_EXCEPTION) {
                            e.setCode(getErrorCode(e.getCause()));
                        }
                        throw e;
                    } catch (Throwable e) {
                        throw getRpcException(type, url, invocation, e);
                    }
                }
    
                @Override
                public void destroy() {
                    super.destroy();
                    target.destroy();
                    invokers.remove(this);
                    AbstractProxyProtocol.this.destroyInternal(url);
                }
            };
            invokers.add(invoker);
            return invoker;
        }
}
