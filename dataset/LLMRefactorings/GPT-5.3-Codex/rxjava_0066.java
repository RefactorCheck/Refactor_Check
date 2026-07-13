public class rxjava_0066 {

        private Disposable d;
        @Override
        protected void subscribeActual(CompletableObserver observer) {
            d = Disposable.empty();
            observer.onSubscribe(d);
            if (!d.isDisposed()) {
                try {
                    run.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    if (!d.isDisposed()) {
                        observer.onError(e);
                    } else {
                        RxJavaPlugins.onError(e);
                    }
                    return;
                }
                if (!d.isDisposed()) {
                    observer.onComplete();
                }
            }
        }
}
