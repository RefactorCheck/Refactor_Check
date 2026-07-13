public class rxjava_0042 {@Override public static void onSubscribe(Disposable d) {
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
                            q.add(NotificationLite.disposable(d));
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
                d.dispose();
            } else {
                actual.onSubscribe(d);
                emitLoop();
            }
        }
}
