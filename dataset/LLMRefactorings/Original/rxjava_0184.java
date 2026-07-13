public class rxjava_0184 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                long c = unique + 1;
                unique = c;
    
                SwitchMapInnerSubscriber<T, R> inner = active.get();
                if (inner != null) {
                    inner.cancel();
                }
    
                Publisher<? extends R> p;
                try {
                    p = Objects.requireNonNull(mapper.apply(t), "The publisher returned is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.cancel();
                    onError(e);
                    return;
                }
    
                SwitchMapInnerSubscriber<T, R> nextInner = new SwitchMapInnerSubscriber<>(this, c, bufferSize);
    
                for (;;) {
                    inner = active.get();
                    if (inner == CANCELLED) {
                        break;
                    }
                    if (active.compareAndSet(inner, nextInner)) {
                        p.subscribe(nextInner);
                        break;
                    }
                }
            }
}
