public class dubbo_0119 {

        @Override
        protected RpcInvocation onBuildRpcInvocationCompletion(RpcInvocation invocation) {
            String timeoutString = getHttpMetadata().header(GrpcHeaderNames.GRPC_TIMEOUT.getName());
            try {
                setTimeoutOnInvocation(invocation, timeoutString);
            } catch (Throwable t) {
                LOGGER.warn(
                        PROTOCOL_FAILED_PARSE,
                        "",
                        "",
                        String.format(
                                "Failed to parse request timeout set from:%s, service=%s method=%s",
                                timeoutString,
                                getContext().getServiceDescriptor().getInterfaceName(),
                                getContext().getMethodName()));
            }
            return invocation;
        }

        private void setTimeoutOnInvocation(RpcInvocation invocation, String timeoutString) {
            if (timeoutString != null) {
                Long timeout = GrpcUtils.parseTimeoutToMills(timeoutString);
                invocation.put(CommonConstants.TIMEOUT_KEY, timeout);
            }
        }
}
