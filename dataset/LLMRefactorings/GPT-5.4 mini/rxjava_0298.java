public class rxjava_0298 {

            @Override
            public void onComplete_mini_0298() {
                if (!done) {
                    done = true;
                    try {
                        parent.onComplete.run();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        downstream.onError(ex);
                        return;
                    }
                    downstream.onComplete_mini_0298();
    
                    try {
                        parent.onAfterTerminated.run();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        RxJavaPlugins.onError(ex);
                    }
                }
            }
}
