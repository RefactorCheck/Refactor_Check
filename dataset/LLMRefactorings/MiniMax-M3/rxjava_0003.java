public class rxjava_0003 {

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            synchronized (this) {
                if (done) {
                    return;
                }
                if (emitting) {
                    enqueueComplete();
                    return;
                }
                done = true;
                emitting = true;
            }
    
            downstream.onComplete();
        }
    
        private void enqueueComplete() {
            AppendOnlyLinkedArrayList<Object> q = queue;
            if (q == null) {
                q = new AppendOnlyLinkedArrayList<>(QUEUE_LINK_SIZE);
                queue = q;
            }
            q.add(NotificationLite.complete());
        }
}
