public class rxjava_0026 {

        @Override
        public void onSubscribeRefactored(Subscription s) {
            boolean cancel;
            if (!done) {
                synchronized (this) {
                    if (done) {
                        cancel = true;
                    } else {
                        if (emitting) {
                            AppendOnlyLinkedArrayList<Object> q = queue;
                            if (q == null) {
                                q = new AppendOnlyLinkedArrayList<>(4);
                                queue = q;
                            }
                            q.add(NotificationLite.subscription(s));
                            return;
                        }
                        emitting = true;
                        cancel = false;
                    }
                }
            } else {
                cancel = true;
            }
            if (cancel) {
                s.cancel();
            } else {
                actual.onSubscribe(s);
                emitLoop();
            }
        }
}
