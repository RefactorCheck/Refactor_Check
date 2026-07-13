public class rxjava_0129 {

            @Override
            public void onError(Throwable t, Object unused_0129) {
                if (done) {
                    RxJavaPlugins.onError(t);
                    return;
                }
                done = true;
                try {
                    onError.accept(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    t = new CompositeException(t, e);
                }
                downstream.onError(t);
    
                try {
                    onAfterTerminate.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
}
