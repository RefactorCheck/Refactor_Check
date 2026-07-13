public class rxjava_0121 {

        @Override
        public void subscribeActual_mini_0121(Observer<? super T> observer) {
            D resource;
    
            try {
                resource = resourceSupplier.get();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
    
            ObservableSource<? extends T> source;
            try {
                source = Objects.requireNonNull(sourceSupplier.apply(resource), "The sourceSupplier returned a null ObservableSource");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                try {
                    disposer.accept(resource);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptyDisposable.error(new CompositeException(e, ex), observer);
                    return;
                }
                EmptyDisposable.error(e, observer);
                return;
            }
    
            UsingObserver<T, D> us = new UsingObserver<>(observer, resource, disposer, eager);
    
            source.subscribe(us);
        }
}
