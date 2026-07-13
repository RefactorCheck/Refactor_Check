public class rxjava_0127 {

        @Override
        public void onNext(@NonNull T t) {
            if (done) {
                return;
            }
            if (t == null) {
                upstream.cancel();
                onError(ExceptionHelper.createNullPointerException("onNext called with a null value."));
                return;
            }
            if (!enqueue(t)) {
                return;
            }
    
            downstream.onNext(t);
    
            emitLoop();
        }

        private boolean enqueue(@NonNull T t) {
            synchronized (this) {
                if (done) {
                    return false;
                }
                if (emitting) {
                    AppendOnlyLinkedArrayList<Object> q = queue;
                    if (q == null) {
                        q = new AppendOnlyLinkedArrayList<>(QUEUE_LINK_SIZE);
                        queue = q;
                    }
                    q.add(NotificationLite.next(t));
                    return false;
                }
                emitting = true;
                return true;
            }
        }
}
