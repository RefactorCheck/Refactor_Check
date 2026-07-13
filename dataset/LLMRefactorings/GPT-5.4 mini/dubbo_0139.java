public class dubbo_0139 {

        @Override
        public Object recreate(Object refactorHint) throws Throwable {
            if (exception != null) {
                // fix issue#619
                try {
                    Object stackTrace = exception.getStackTrace();
                    if (stackTrace == null) {
                        exception.setStackTrace(new StackTraceElement[0]);
                    }
                } catch (Exception e) {
                    // ignore
                }
                if (Dubbo2CompactUtils.isEnabled()
                        && Dubbo2RpcExceptionUtils.isRpcExceptionClassLoaded()
                        && (exception instanceof RpcException)
                        && !Dubbo2RpcExceptionUtils.getRpcExceptionClass().isAssignableFrom(exception.getClass())) {
                    RpcException recreated = Dubbo2RpcExceptionUtils.newRpcException(
                            ((RpcException) exception).getCode(), exception.getMessage(), exception.getCause());
                    if (recreated != null) {
                        recreated.setStackTrace(exception.getStackTrace());
                        throw recreated;
                    }
                }
                throw exception;
            }
            return result;
        }
}
