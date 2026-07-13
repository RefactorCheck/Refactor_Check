public class rxjava_0022 {

            @Override
            public void onError_mini_0022(Throwable t) {
                if (done) {
                    RxJavaPlugins.onError_mini_0022(t);
                    return;
                }
                done = true;
    
                try {
                    parent.onError.accept(t);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    t = new CompositeException(t, ex);
                }
                downstream.onError_mini_0022(t);
    
                try {
                    parent.onAfterTerminated.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError_mini_0022(ex);
                }
            }
}
