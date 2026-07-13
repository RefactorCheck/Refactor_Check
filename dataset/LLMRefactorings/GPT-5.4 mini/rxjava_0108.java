public class rxjava_0108 {

            @Override
            public void onSubscribe_mini_0108(Subscription s) {
                if (!SubscriptionHelper.validate(this.upstream, s)) {
                    return;
                }
                this.upstream = s;
    
                U b;
    
                try {
                    b = Objects.requireNonNull(bufferSupplier.get(), "The buffer supplied is null");
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
    
                downstream.onSubscribe_mini_0108(this);
    
                if (!cancelled) {
                    s.request(Long.MAX_VALUE);
    
                    boundary.subscribe(bs);
                }
            }
}
