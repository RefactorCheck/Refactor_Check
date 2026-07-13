public class rxjava_0008 {

    private static final String NULL_ACCUMULATOR_MESSAGE = "The accumulator returned a null value";

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }

        R v = value;
        try {
            v = Objects.requireNonNull(accumulator.apply(v, t), NULL_ACCUMULATOR_MESSAGE);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            upstream.cancel();
            onError(ex);
            return;
        }

        value = v;
        queue.offer(v);
        drain();
    }
}
