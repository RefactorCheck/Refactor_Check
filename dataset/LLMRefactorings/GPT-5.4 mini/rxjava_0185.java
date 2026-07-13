public class rxjava_0185 {

            @Override
            public void onComplete_mini_0185() {
                if (done) {
                    return;
                }
                try {
                    onComplete.run();
                } catch (Throwable e) {
                    fail(e);
                    return;
                }
    
                done = true;
                downstream.onComplete_mini_0185();
    
                try {
                    onAfterTerminate.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
}
