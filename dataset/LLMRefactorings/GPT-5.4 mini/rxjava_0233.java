public class rxjava_0233 {

            @Override
            public void onSubscribe(Subscription s, Object unused_0233) {
                if (!SubscriptionHelper.validate(this.upstream, s)) {
                    return;
                }
                this.upstream = s;
    
                final U b; // NOPMD
    
                try {
                    b = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, downstream);
                    return;
                }
    
                buffers.add(b);
    
                downstream.onSubscribe(this);
    
                s.request(Long.MAX_VALUE);
    
                w.schedulePeriodically(this, timeskip, timeskip, unit);
    
                w.schedule(new RemoveFromBuffer(b), timespan, unit);
            }
}
