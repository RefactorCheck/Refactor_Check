public class dubbo_0253 {

        @Override
        public Result invoke(Invocation inv) throws RpcException {
            // if invoker is destroyed due to address refresh from registry, let's allow the current invoke to proceed
            if (isDestroyed()) {
                logDestroyedWarning();
            }
    
            RpcInvocation invocation = (RpcInvocation) inv;
    
            // prepare rpc invocation
            prepareInvocation(invocation);
    
            // do invoke rpc invocation and return async result
            AsyncRpcResult asyncResult = doInvokeAndReturn(invocation);
    
            // wait rpc result if sync
            waitForResultIfSync(asyncResult, invocation);
    
            return asyncResult;
        }

        private void logDestroyedWarning() {
            logger.warn(
                    PROTOCOL_FAILED_REQUEST,
                    "",
                    "",
                    "Invoker for service " + this + " on consumer " + NetUtils.getLocalHost() + " is destroyed, "
                            + ", dubbo version is " + Version.getVersion()
                            + ", this invoker should not be used any longer");
        }
}
