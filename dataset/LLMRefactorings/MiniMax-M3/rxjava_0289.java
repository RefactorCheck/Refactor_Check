public class rxjava_0289 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                long idx = index + 1;
                index = idx;
    
                Disposable d = debouncer.get();
                if (d != null) {
                    d.dispose();
                }
    
                Publisher<U> p;
    
                try {
                    p = Objects.requireNonNull(debounceSelector.apply(t), "The publisher supplied is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    downstream.onError(e);
                    return;
                }
    
                subscribeInner(p, d, idx, t);
            }
            
            private void subscribeInner(Publisher<U> p, Disposable d, long idx, T t) {
                DebounceInnerSubscriber<T, U> inner = new DebounceInnerSubscriber<>(this, idx, t);
                if (debouncer.compareAndSet(d, inner)) {
                    p.subscribe(inner);
                }
            }
}
