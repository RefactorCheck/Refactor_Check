public class rxjava_0042 {

        @Override
        public void onSubscribe(Disposable d) {
            if (handleSubscription(d)) {
                return;
            }
            actual.onSubscribe(d);
            emitLoop();
        }

        private boolean handleSubscription(Disposable d) {
            if (done) {
                d.dispose();
                return true;
            }
            synchronized (this) {
                if (done) {
                    d.dispose();
                    return true;
                }
                if (emitting) {
                    AppendOnlyLinkedArrayList<Object> q = queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(4);
                        queue = q;
                    }
                    q.add(NotificationLite.disposable(d));
                    return true;
                }
                emitting = true;
                return false;
            }
        }
}
