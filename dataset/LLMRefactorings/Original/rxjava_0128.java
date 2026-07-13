public class rxjava_0128 {

        @Override
        public void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
            boolean reportError;
            synchronized (this) {
                if (done) {
                    reportError = true;
                } else
                if (emitting) {
                    done = true;
                    AppendOnlyLinkedArrayList<Object> q = queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(QUEUE_LINK_SIZE);
                        queue = q;
                    }
                    Object err = NotificationLite.error(t);
                    if (delayError) {
                        q.add(err);
                    } else {
                        q.setFirst(err);
                    }
                    return;
                } else {
                    done = true;
                    emitting = true;
                    reportError = false;
                }
            }
    
            if (reportError) {
                RxJavaPlugins.onError(t);
                return;
            }
    
            downstream.onError(t);
            // no need to loop because this onError is the last event
        }
}
