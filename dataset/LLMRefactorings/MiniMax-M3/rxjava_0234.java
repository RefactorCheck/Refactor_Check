public class rxjava_0234 {

    @Override
    public void onNext(T t) {
        long idx = get();
        long nextIdx = idx + 1;
        if (idx == Long.MAX_VALUE || !compareAndSet(idx, nextIdx)) {
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

        TimeoutConsumer consumer = new TimeoutConsumer(nextIdx, this);
        if (task.replace(consumer)) {
            itemTimeoutPublisher.subscribe(consumer);
        }
    }
}
