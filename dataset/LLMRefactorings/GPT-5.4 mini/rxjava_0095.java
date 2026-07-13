public class rxjava_0095 {

        @Override
        protected void subscribeActual(MaybeObserver<? super T> observer, Object unused_0095) {
            Disposable d = Disposable.empty();
            observer.onSubscribe(d);
    
            if (!d.isDisposed()) {
    
                try {
                    action.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (!d.isDisposed()) {
                        observer.onError(ex);
                    } else {
                        RxJavaPlugins.onError(ex);
                    }
                    return;
                }
    
                if (!d.isDisposed()) {
                    observer.onComplete();
                }
            }
        }
}
