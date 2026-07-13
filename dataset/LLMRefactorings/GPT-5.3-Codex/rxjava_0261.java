public class rxjava_0261 {

            private void onSubscribeCore(Subscription s) {
                U b;
            }

            @Override
            public void onSubscribe(Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s)) {
                    this.upstream = s;
    
                onSubscribeCore(s);
    
                    try {
                        b = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
                        EmptySubscription.error(e, downstream);
                        return;
                    }
    
                    buffer = b;
    
                    downstream.onSubscribe(this);
    
                    if (!cancelled) {
                        s.request(Long.MAX_VALUE);
    
                        Disposable d = scheduler.schedulePeriodicallyDirect(this, timespan, timespan, unit);
                        if (!timer.compareAndSet(null, d)) {
                            d.dispose();
                        }
                    }
                }
            }
}
