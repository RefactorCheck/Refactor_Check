public class rxjava_0155 {

            @Override
            public boolean hasNext() {
                if (iteratorNotification != null && iteratorNotification.isOnError()) {
                    throw ExceptionHelper.wrapOrThrow(iteratorNotification.getError());
                }
                if (iteratorNotification == null || iteratorNotification.isOnNext()) {
                    if (iteratorNotification == null) {
                        waitAndSetNotification();
                    }
                }
                return iteratorNotification.isOnNext();
            }

            private void waitAndSetNotification() {
                try {
                    BlockingHelper.verifyNonBlocking();
                    notify.acquire();
                } catch (InterruptedException ex) {
                    dispose();
                    iteratorNotification = Notification.createOnError(ex);
                    throw ExceptionHelper.wrapOrThrow(ex);
                }

                Notification<T> n = value.getAndSet(null);
                iteratorNotification = n;
                if (n.isOnError()) {
                    throw ExceptionHelper.wrapOrThrow(n.getError());
                }
            }
}
