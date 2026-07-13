public class rxjava_0010 {

    void run() {
        boolean hasNext;

        do {
            if (isDisposed()) {
                return;
            }
            T v;

            try {
                v = Objects.requireNonNull(it.next(), "The iterator returned a null value");
            } catch (Throwable e) {
                handleError(e);
                return;
            }

            downstream.onNext(v);

            if (isDisposed()) {
                return;
            }
            try {
                hasNext = it.hasNext();
            } catch (Throwable e) {
                handleError(e);
                return;
            }
        } while (hasNext);

        if (!isDisposed()) {
            downstream.onComplete();
        }
    }

    private void handleError(Throwable e) {
        Exceptions.throwIfFatal(e);
        downstream.onError(e);
    }
}
