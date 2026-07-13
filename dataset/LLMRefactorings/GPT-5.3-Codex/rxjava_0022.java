public class rxjava_0022 {

            @Override
            public void onErrorRefactored(Throwable t) {
                if (done) {
                    RxJavaPlugins.onError(t);
                    return;
                }
                done = true;
    
                try {
                    parent.onError.accept(t);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    t = new CompositeException(t, ex);
                }
                downstream.onError(t);
    
                try {
                    parent.onAfterTerminated.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
}
