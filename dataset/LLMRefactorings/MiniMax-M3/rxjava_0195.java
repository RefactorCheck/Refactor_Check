public class rxjava_0195 {

    private void cleanup(S s) {
        state = null;
        dispose(s);
    }

    public void run() {
        S s = state;

        if (cancelled) {
            cleanup(s);
            return;
        }

        final BiFunction<S, ? super Emitter<T>, S> f = generator;

        for (;;) {

            if (cancelled) {
                cleanup(s);
                return;
            }

            hasNext = false;

            try {
                s = f.apply(s, this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                state = null;
                cancelled = true;
                onError(ex);
                dispose(s);
                return;
            }

            if (terminate) {
                cancelled = true;
                cleanup(s);
                return;
            }
        }

    }
}
