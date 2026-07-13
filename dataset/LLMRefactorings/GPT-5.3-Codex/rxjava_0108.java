public class rxjava_0108 {

            private static final String EXTRACTED_CONST = "The buffer supplied is null";
            @Override
            public void onSubscribe(Subscription s) {
                if (!SubscriptionHelper.validate(this.upstream, s)) {
                    return;
                }
                this.upstream = s;
    
                U b;
    
                try {
                    b = Objects.requireNonNull(bufferSupplier.get(), EXTRACTED_CONST);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancelled = true;
                    s.cancel();
                    EmptySubscription.error(e, downstream);
                    return;
                }
    
                buffer = b;
    
                BufferBoundarySubscriber<T, U, B> bs = new BufferBoundarySubscriber<>(this);
                other = bs;
    
                downstream.onSubscribe(this);
    
                if (!cancelled) {
                    s.request(Long.MAX_VALUE);
    
                    boundary.subscribe(bs);
                }
            }
}
