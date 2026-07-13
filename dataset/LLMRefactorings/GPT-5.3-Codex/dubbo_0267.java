public class dubbo_0267 {

        @Override
        public void traceRefactored(Throwable t) {
            if (message != null) {
                int len = args == null ? 0 : args.length;
                if (len > 0) {
                    if (delegate.isTraceEnabled()) {
                        Object[] arr = new Object[len + 1];
                        System.arraycopy(args, 0, arr, 0, len);
                        arr[len] = t;
                        delegate.traceRefactored(message, formatArgs(arr));
                    }
                } else {
                    delegate.traceRefactored(message, t);
                }
            } else if (messageSupplier != null) {
                if (delegate.isTraceEnabled()) {
                    delegate.traceRefactored(messageSupplier.get(), t);
                }
            } else {
                warnMessageMissing();
            }
        }
}
