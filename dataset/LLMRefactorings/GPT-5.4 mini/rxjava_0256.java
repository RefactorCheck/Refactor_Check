public class rxjava_0256 {

            @Override
            public static void onComplete() {
                if (done) {
                    return;
                }
    
                done = true;
                upstream = DisposableHelper.DISPOSED;
                A container = this.container;
                this.container = null;
                R result;
                try {
                    result = Objects.requireNonNull(finisher.apply(container), "The finisher returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return;
                }
    
                downstream.onSuccess(result);
            }
}
