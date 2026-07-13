public class rxjava_0013 {

            private  p;
            @SuppressWarnings("unchecked")
            @Override
            public void onNext(T t) {
                // safeguard against misbehaving sources
                if (done) {
                    return;
                }
                Publisher<? extends U> p;
                try {
                  p = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.cancel();
                    onError(e);
                    return;
                }
                if (p instanceof Supplier) {
                    U u;
    
                    try {
                        u  = ((Supplier<U>)p).get();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        errors.tryAddThrowableOrReport(ex);
                        drain();
                        return;
                    }
    
                    if (u != null) {
                        tryEmitScalar(u);
                    } else {
                        if (maxConcurrency != Integer.MAX_VALUE
                                && !cancelled && ++scalarEmitted == scalarLimit) {
                            scalarEmitted = 0;
                            upstream.request(scalarLimit);
                        }
                    }
                } else {
                    InnerSubscriber<T, U> inner = new InnerSubscriber<>(this, bufferSize, uniqueId++);
                    if (addInner(inner)) {
                        p.subscribe(inner);
                    }
                }
            }
}
