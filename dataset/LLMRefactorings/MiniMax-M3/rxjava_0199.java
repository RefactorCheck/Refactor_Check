public class rxjava_0199 {

    @SuppressWarnings("unchecked")
    @Override
    public void onComplete() {
        upstream = DisposableHelper.DISPOSED;
        if (eager) {
            if (disposeEagerly()) {
                return;
            }
        }
        downstream.onComplete();
        if (!eager) {
            disposeResource();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean disposeEagerly() {
        Object resource = getAndSet(this);
        if (resource == this) {
            return true;
        }
        try {
            disposer.accept((R) resource);
            return false;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return true;
        }
    }
}
