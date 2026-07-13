public class rxjava_0194 {

    @SuppressWarnings("unchecked")
    @Override
    public void onError(Throwable e) {
        upstream = DisposableHelper.DISPOSED;

        if (eager) {
            Object u = getAndSet(this);
            if (u == this) {
                return;
            }
            e = disposeAndWrap(u, e);
        }

        downstream.onError(e);

        if (!eager) {
            disposeResource();
        }
    }

    @SuppressWarnings("unchecked")
    private Throwable disposeAndWrap(Object u, Throwable e) {
        try {
            disposer.accept((U)u);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            return new CompositeException(e, ex);
        }
        return e;
    }
}
