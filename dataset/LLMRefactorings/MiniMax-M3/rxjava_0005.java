public class rxjava_0005 {

    @Override
    public void onError(Throwable t) {
        if (once) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
            downstream.onError(t);
            return;
        }
        once = true;

        ObservableSource<? extends T> p = resolveNextSource(t);
        if (p != null) {
            p.subscribe(this);
        }
    }

    private ObservableSource<? extends T> resolveNextSource(Throwable t) {
        ObservableSource<? extends T> p;
        try {
            p = nextSupplier.apply(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            downstream.onError(new CompositeException(t, e));
            return null;
        }

        if (p == null) {
            NullPointerException npe = new NullPointerException("Observable is null");
            npe.initCause(t);
            downstream.onError(npe);
            return null;
        }

        return p;
    }
}
