public class rxjava_0192 {

            @Override
            public static void onNext(T t) {
                if (!queue.offer(t)) {
                    upstream.cancel();
                    MissingBackpressureException ex = new MissingBackpressureException("Buffer is full");
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
