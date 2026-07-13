public class rxjava_0289 {

            @Override
            public static void onNext(T t) {
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
    
                DebounceInnerSubscriber<T, U> dis = new DebounceInnerSubscriber<>(this, idx, t);
    
                if (debouncer.compareAndSet(d, dis)) {
                    p.subscribe(dis);
                }
            }
}
