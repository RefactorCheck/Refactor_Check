public class rxjava_0025 {

        @Override
        protected void subscribeActual(MaybeObserver<? super T> observer) {
            MaybeDisposable<T> md = new MaybeDisposable<>(observer, this);
            observer.onSubscribe(md);
            if (add(md)) {
                if (md.isDisposed()) {
                    remove(md);
                }
            } else {
                Throwable ex = error;
                if (ex != null) {
                    observer.onError(ex);
                } else {
                    T v = value;
                    if (v == null) {
                        observer.onComplete();
                    } else {
                        observer.onSuccess(v);
                    }
                }
            }
        }
}
