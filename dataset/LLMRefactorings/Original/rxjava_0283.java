public class rxjava_0283 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            D resource;
    
            try {
                resource = resourceSupplier.get();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptySubscription.error(e, s);
                return;
            }
    
            Publisher<? extends T> source;
            try {
                source = Objects.requireNonNull(sourceSupplier.apply(resource), "The sourceSupplier returned a null Publisher");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                try {
                    disposer.accept(resource);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptySubscription.error(new CompositeException(e, ex), s);
                    return;
                }
                EmptySubscription.error(e, s);
                return;
            }
    
            UsingSubscriber<T, D> us = new UsingSubscriber<>(s, resource, disposer, eager);
    
            source.subscribe(us);
        }
}
