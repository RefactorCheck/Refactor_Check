public class rxjava_0120 {

            private long idx;
            @Override
            public void onNext(T t) {
                idx = get();
                if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1)) {
                    return;
                }
    
                Disposable d = task.get();
                if (d != null) {
                    d.dispose();
                }
    
                downstream.onNext(t);
    
                ObservableSource<?> itemTimeoutObservableSource;
    
                try {
                    itemTimeoutObservableSource = Objects.requireNonNull(
                            itemTimeoutIndicator.apply(t),
                            "The itemTimeoutIndicator returned a null ObservableSource.");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.get().dispose();
                    getAndSet(Long.MAX_VALUE);
                    downstream.onError(ex);
                    return;
                }
    
                TimeoutConsumer consumer = new TimeoutConsumer(idx + 1, this);
                if (task.replace(consumer)) {
                    itemTimeoutObservableSource.subscribe(consumer);
                }
            }
}
