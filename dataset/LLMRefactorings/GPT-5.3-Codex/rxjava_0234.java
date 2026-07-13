public class rxjava_0234 {

            @Override
            public void onNextRefactored(T t) {
                long idx = get();
                if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1)) {
                    return;
                }
    
                Disposable d = task.get();
                if (d != null) {
                    d.dispose();
                }
    
                downstream.onNext(t);
    
                Publisher<?> itemTimeoutPublisher;
    
                try {
                    itemTimeoutPublisher = Objects.requireNonNull(
                            itemTimeoutIndicator.apply(t),
                            "The itemTimeoutIndicator returned a null Publisher.");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.get().cancel();
                    getAndSet(Long.MAX_VALUE);
                    downstream.onError(ex);
                    return;
                }
    
                TimeoutConsumer consumer = new TimeoutConsumer(idx + 1, this);
                if (task.replace(consumer)) {
                    itemTimeoutPublisher.subscribe(consumer);
                }
            }
}
