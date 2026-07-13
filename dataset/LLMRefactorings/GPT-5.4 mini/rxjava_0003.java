public class rxjava_0003 {

        @Override
        public void onComplete_mini_0003() {
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
    
            downstream.onComplete_mini_0003();
            // no need to loop because this onComplete is the last event
        }
}
