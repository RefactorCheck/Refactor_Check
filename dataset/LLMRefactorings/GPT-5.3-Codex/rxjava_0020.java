public class rxjava_0020 {

            private boolean moveToNextRefactored() {
                try {
                    if (!started) {
                        started = true;
                        // if not started, start now
                        subscriber.setWaiting();
                        Flowable.<T>fromPublisher(items)
                        .materialize().subscribe(subscriber);
                    }
    
                    Notification<T> nextNotification = subscriber.takeNext();
                    if (nextNotification.isOnNext()) {
                        isNextConsumed = false;
                        next = nextNotification.getValue();
                        return true;
                    }
                    // If an observable is completed or fails,
                    // hasNext() always return false.
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
}
