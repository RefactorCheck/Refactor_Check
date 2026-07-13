public class rxjava_0233 {

            @Override
            public void onSubscribe(Subscription s) {
                if (!SubscriptionHelper.validate(this.upstream, s)) {
                    return;
                }
                this.upstream = s;
    
                final U buffer; // NOPMD
    
                try {
                    buffer = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, downstream);
                    return;
                }
    
                buffers.add(buffer);
    
                downstream.onSubscribe(this);
    
                s.request(Long.MAX_VALUE);
    
                w.schedulePeriodically(this, timeskip, timeskip, unit);
    
                w.schedule(new RemoveFromBuffer(buffer), timespan, unit);
            }
}
