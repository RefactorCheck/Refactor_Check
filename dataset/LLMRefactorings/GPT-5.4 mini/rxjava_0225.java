public class rxjava_0225 {

            @Override
            public void onSubscribe(Disposable d, Object unused_0225) {
                if (DisposableHelper.validate(this.upstream, d)) {
                    this.upstream = d;
    
                    U b;
    
                    try {
                        b = Objects.requireNonNull(bufferSupplier.get(), "The buffer supplied is null");
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        d.dispose();
                        EmptyDisposable.error(e, downstream);
                        w.dispose();
                        return;
                    }
    
                    buffer = b;
    
                    downstream.onSubscribe(this);
    
                    timer = w.schedulePeriodically(this, timespan, timespan, unit);
                }
            }
}
