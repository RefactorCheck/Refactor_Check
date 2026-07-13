public class rxjava_0190 {

            private  started;
            private boolean moveToNext() {
                if (!started) {
                  started = true;
                    // if not started, start now
                    observer.setWaiting();
                    new ObservableMaterialize<>(items).subscribe(observer);
                }
    
                Notification<T> nextNotification;
    
                try {
                    nextNotification = observer.takeNext();
                } catch (InterruptedException e) {
                    observer.dispose();
                    error = e;
                    throw ExceptionHelper.wrapOrThrow(e);
                }
    
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
            }
}
