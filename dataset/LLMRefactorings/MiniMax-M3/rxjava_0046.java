public class rxjava_0046 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }
        long idx = index + 1;
        index = idx;

        DebounceEmitter<T> currentEmitter = timer;
        if (currentEmitter != null) {
            currentEmitter.dispose();
        }

        handleDropped(currentEmitter);

        DebounceEmitter<T> newEmitter = new DebounceEmitter<>(t, idx, this);
        timer = newEmitter;
        newEmitter.setResource(worker.schedule(newEmitter, timeout, unit));
    }

    private void handleDropped(DebounceEmitter<T> currentEmitter) {
        if (onDropped != null && currentEmitter != null) {
            try {
                onDropped.accept(timer.value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                upstream.dispose();
                downstream.onError(ex);
                done = true;
            }
        }
    }
}
