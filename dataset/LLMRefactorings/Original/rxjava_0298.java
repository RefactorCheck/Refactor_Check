public class rxjava_0298 {

            @Override
            public void onComplete() {
                if (!done) {
                    done = true;
                    try {
                        parent.onComplete.run();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        downstream.onError(ex);
                        return;
                    }
                    downstream.onComplete();
    
                    try {
                        parent.onAfterTerminated.run();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        RxJavaPlugins.onError(ex);
                    }
                }
            }
}
