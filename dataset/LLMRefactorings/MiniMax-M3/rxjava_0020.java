public class rxjava_0020 {

    private boolean moveToNext() {
        try {
            if (!started) {
                startSubscription();
            }

            Notification<T> nextNotification = subscriber.takeNext();
            if (nextNotification.isOnNext()) {
                isNextConsumed = false;
                next = nextNotification.getValue();
                return true;
            }
            hasNext = false;
            if (nextNotification.isOnComplete()) {
                return false;
            }
            error = nextNotification.getError();
            throw ExceptionHelper.wrapOrThrow(error);
        } catch (InterruptedException e) {
            subscriber.dispose();
            error = e;
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    private void startSubscription() {
        started = true;
        subscriber.setWaiting();
        Flowable.<T>fromPublisher(items)
        .materialize().subscribe(subscriber);
    }
}
