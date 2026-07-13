public class rxjava_0190 {

    private boolean moveToNext() {
        if (!started) {
            started = true;
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

        return processNotification(nextNotification);
    }

    private boolean processNotification(Notification<T> nextNotification) {
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
    }
}
