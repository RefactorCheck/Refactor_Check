public class rxjava_0285 {

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            synchronized (this) {
                if (done) {
                    return;
                }
                done = true;
                if (emitting) {
                    AppendOnlyLinkedArrayList<Object> q = queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(4);
                        queue = q;
                    }
                    q.add(NotificationLite.complete());
                    return;
                }
                emitting = true;
            }
            actual.onComplete();
        }
}
