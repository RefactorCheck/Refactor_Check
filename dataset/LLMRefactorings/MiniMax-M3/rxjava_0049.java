public class rxjava_0049 {

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(T value) {
        upstream = DisposableHelper.DISPOSED;

        if (eager && !handleEagerDisposal()) {
            return;
        }

        downstream.onSuccess(value);

        if (!eager) {
            disposeResource();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean handleEagerDisposal() {
        Object u = getAndSet(this);
        if (u == this) {
            return false;
        }
        try {
            disposer.accept((U)u);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return false;
        }
        return true;
    }
}
