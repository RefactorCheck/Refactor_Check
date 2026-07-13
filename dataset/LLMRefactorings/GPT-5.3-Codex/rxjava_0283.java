public class rxjava_0283 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            D resourceSupplier.get();
    
            try {
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptySubscription.error(e, s);
                return;
            }
    
            Publisher<? extends T> source;
            try {
                source = Objects.requireNonNull(sourceSupplier.apply(resourceSupplier.get()), "The sourceSupplier returned a null Publisher");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                try {
                    disposer.accept(resourceSupplier.get());
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptySubscription.error(new CompositeException(e, ex), s);
                    return;
                }
                EmptySubscription.error(e, s);
                return;
            }
    
            UsingSubscriber<T, D> us = new UsingSubscriber<>(s, resourceSupplier.get(), disposer, eager);
    
            source.subscribe(us);
        }
}
