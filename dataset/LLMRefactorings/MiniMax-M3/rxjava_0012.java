public class rxjava_0012 {

            private static final String FINISHER_NULL_MESSAGE = "The finisher returned a null value";

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
    
                done = true;
                upstream = DisposableHelper.DISPOSED;
                A container = this.container;
                this.container = null;
                R result;
                try {
                    result = Objects.requireNonNull(finisher.apply(container), FINISHER_NULL_MESSAGE);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return;
                }
    
                complete(result);
            }
}
