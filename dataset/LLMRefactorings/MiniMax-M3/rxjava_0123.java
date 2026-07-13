public class rxjava_0123 {

        @Override
        public void onNext(@NonNull T t) {
            if (done) {
                return;
            }
            if (t == null) {
                handleNullValue();
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
                    q.add(NotificationLite.next(t));
                    return;
                }
                emitting = true;
            }
    
            downstream.onNext(t);
    
            emitLoop();
        }
    
        private void handleNullValue() {
            upstream.dispose();
            onError(ExceptionHelper.createNullPointerException("onNext called with a null value."));
        }
}
