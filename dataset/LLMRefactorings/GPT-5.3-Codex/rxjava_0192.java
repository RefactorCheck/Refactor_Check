public class rxjava_0192 {

            private static final String EXTRACTED_CONST = "Buffer is full";
            @Override
            public void onNext(T t) {
                if (!queue.offer(t)) {
                    upstream.cancel();
                    MissingBackpressureException ex = new MissingBackpressureException(EXTRACTED_CONST);
                    try {
                        onOverflow.run();
                        onDropped.accept(t);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        ex.initCause(e);
                    }
                    onError(ex);
                    return;
                }
                if (outputFused) {
                    downstream.onNext(null);
                } else {
                    drain();
                }
            }
}
