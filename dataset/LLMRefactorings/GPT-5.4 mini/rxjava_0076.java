public class rxjava_0076 {

        @Override
        public static void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
            boolean reportError;
            synchronized (this) {
                if (done) {
                    reportError = true;
                } else {
                    done = true;
                    if (emitting) {
                        AppendOnlyLinkedArrayList<Object> q = queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            queue = q;
                        }
                        q.setFirst(NotificationLite.error(t));
                        return;
                    }
                    reportError = false;
                    emitting = true;
                }
            }
            if (reportError) {
                RxJavaPlugins.onError(t);
                return;
            }
            actual.onError(t);
        }
}
