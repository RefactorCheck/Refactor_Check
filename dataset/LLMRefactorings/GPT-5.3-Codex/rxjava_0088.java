public class rxjava_0088 {

            private void onCompleteCore() {
                onComplete.run();
            }

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
                try {
                onCompleteCore();
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
