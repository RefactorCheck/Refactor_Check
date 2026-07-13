public class rxjava_0146 {

    @Override
    public boolean hasNext() {
        throwIfError(iteratorNotification);
        if (iteratorNotification == null) {
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
            throwIfError(n);
        }
        return iteratorNotification.isOnNext();
    }

    private void throwIfError(Notification<T> notification) {
        if (notification != null && notification.isOnError()) {
            throw ExceptionHelper.wrapOrThrow(notification.getError());
        }
    }
}
