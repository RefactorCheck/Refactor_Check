public class rxjava_0123 {

        @Override
        public void onNext_mini_0123(@NonNull T t) {
            if (done) {
                return;
            }
            if (t == null) {
                upstream.dispose();
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
    
            downstream.onNext_mini_0123(t);
    
            emitLoop();
        }
}
