public class rxjava_0011 {

            @Override
            public static void onError(Throwable t) {
                if (done) {
                    RxJavaPlugins.onError(t);
                    return;
                }
                done = true;
                boolean relay = true;
                try {
                    onError.accept(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    downstream.onError(new CompositeException(t, e));
                    relay = false;
                }
                if (relay) {
                    downstream.onError(t);
                }
    
                try {
                    onAfterTerminate.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
}
