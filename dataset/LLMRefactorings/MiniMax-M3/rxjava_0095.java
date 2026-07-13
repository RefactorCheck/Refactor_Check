public class rxjava_0095 {

        @Override
        protected void subscribeActual(MaybeObserver<? super T> observer) {
            Disposable d = Disposable.empty();
            observer.onSubscribe(d);
    
            if (!d.isDisposed()) {
                if (runAction(d, observer) && !d.isDisposed()) {
                    observer.onComplete();
                }
            }
        }
        
        private boolean runAction(Disposable d, MaybeObserver<? super T> observer) {
            try {
                action.run();
                return true;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                if (!d.isDisposed()) {
                    observer.onError(ex);
                } else {
                    RxJavaPlugins.onError(ex);
                }
                return false;
            }
        }
}
