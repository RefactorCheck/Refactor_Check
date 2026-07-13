public class rxjava_0208 {

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(T value) {
        upstream = DisposableHelper.DISPOSED;
        if (eager && !disposeEagerly()) {
            return;
        }

        downstream.onSuccess(value);

        if (!eager) {
            disposeResource();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean disposeEagerly() {
        Object resource = getAndSet(this);
        if (resource == this) {
            return false;
        }
        try {
            disposer.accept((D)resource);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return false;
        }
        return true;
    }
}
