public class rxjava_0299 {

    @SuppressWarnings("unchecked")
    @Override
    public void onError(Throwable e) {
        upstream = DisposableHelper.DISPOSED;
        if (eager) {
            e = disposeEagerly(e);
            if (e == null) {
                return;
            }
        }

        downstream.onError(e);

        if (!eager) {
            disposeResource();
        }
    }

    @SuppressWarnings("unchecked")
    private Throwable disposeEagerly(Throwable e) {
        Object resource = getAndSet(this);
        if (resource == this) {
            return null;
        }
        try {
            disposer.accept((R)resource);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            e = new CompositeException(e, ex);
        }
        return e;
    }
}
