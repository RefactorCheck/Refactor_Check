public class dubbo_0267 {

        @Override
        public void trace(Throwable t, Object refactorHint) {
            if (message != null) {
                int len = args == null ? 0 : args.length;
                if (len > 0) {
                    if (delegate.isTraceEnabled()) {
                        Object[] arr = new Object[len + 1];
                        System.arraycopy(args, 0, arr, 0, len);
                        arr[len] = t;
                        delegate.trace(message, formatArgs(arr));
                    }
                } else {
                    delegate.trace(message, t);
                }
            } else if (messageSupplier != null) {
                if (delegate.isTraceEnabled()) {
                    delegate.trace(messageSupplier.get(), t);
                }
            } else {
                warnMessageMissing();
            }
        }
}
