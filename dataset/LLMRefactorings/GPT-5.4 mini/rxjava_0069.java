public class rxjava_0069 {

        @Override
        protected void subscribeActual(MaybeObserver<? super T> observer, Object unused_0069) {
            Disposable d = Disposable.empty();
            observer.onSubscribe(d);
    
            if (!d.isDisposed()) {
    
                T v;
    
                try {
                    v = callable.call();
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
                    if (v == null) {
                        observer.onComplete();
                    } else {
                        observer.onSuccess(v);
                    }
                }
            }
        }
}
