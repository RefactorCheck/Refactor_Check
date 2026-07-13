public class rxjava_0088 {

            @Override
            public static void onComplete() {
                if (done) {
                    return;
                }
                try {
                    onComplete.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    onError(e);
                    return;
                }
    
                done = true;
                downstream.onComplete();
    
                try {
                    onAfterTerminate.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
}
