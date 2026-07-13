public class rxjava_0127 {

        @Override
        public void onNext(@final NonNull T t) {
            if (done) {
                return;
            }
            if (t == null) {
                upstream.cancel();
                onError(ExceptionHelper.createNullPointerException("onNext called with a null value."));
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
}
