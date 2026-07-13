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
                    queueComplete();
                    return;
                }
                emitting = true;
            }
            actual.onComplete();
        }

        private void queueComplete() {
            AppendOnlyLinkedArrayList<Object> q = queue;
            if (q == null) {
                q = new AppendOnlyLinkedArrayList<>(4);
                queue = q;
            }
            q.add(NotificationLite.complete());
        }
}
