public class rxjava_0121 {

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        D resource = acquireResource(observer);
        if (resource == null) {
            return;
        }

        ObservableSource<? extends T> source = acquireSource(resource, observer);
        if (source == null) {
            return;
        }

        UsingObserver<T, D> us = new UsingObserver<>(observer, resource, disposer, eager);
        source.subscribe(us);
    }

    private D acquireResource(Observer<? super T> observer) {
        try {
            return resourceSupplier.get();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, observer);
            return null;
        }
    }

    private ObservableSource<? extends T> acquireSource(D resource, Observer<? super T> observer) {
        try {
            return Objects.requireNonNull(sourceSupplier.apply(resource), "The sourceSupplier returned a null ObservableSource");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            try {
                disposer.accept(resource);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptyDisposable.error(new CompositeException(e, ex), observer);
                return null;
            }
            EmptyDisposable.error(e, observer);
            return null;
        }
    }
}
