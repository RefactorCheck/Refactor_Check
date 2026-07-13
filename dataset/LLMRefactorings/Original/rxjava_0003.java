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
                    AppendOnlyLinkedArrayList<Object> q = queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(QUEUE_LINK_SIZE);
                        queue = q;
                    }
                    q.add(NotificationLite.complete());
                    return;
                }
                done = true;
                emitting = true;
            }
    
            downstream.onComplete();
            // no need to loop because this onComplete is the last event
        }
}
