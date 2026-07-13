public class rxjava_0096 {

            @Override
            public void onError(Throwable t) {
                if (done) {
                    RxJavaPlugins.onError(t);
                    return;
                }
                done = true;
                try {
                    onError.accept(t);
                    downstream.onError(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    downstream.onError(new CompositeException(t, e));
                }

                try {
                    onAfterTerminate.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
}
