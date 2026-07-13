public class rxjava_0225 {

            @Override
            public void onSubscribe(Disposable d) {
                if (DisposableHelper.validate(this.upstream, d)) {
                    this.upstream = d;
                    if (createBuffer(d)) {
                        downstream.onSubscribe(this);
                        timer = w.schedulePeriodically(this, timespan, timespan, unit);
                    }
                }
            }

            private boolean createBuffer(Disposable d) {
                try {
                    U b = Objects.requireNonNull(bufferSupplier.get(), "The buffer supplied is null");
                    buffer = b;
                    return true;
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    d.dispose();
                    EmptyDisposable.error(e, downstream);
                    w.dispose();
                    return false;
                }
            }
}
