public class rxjava_0025 {

        @Override
        protected void subscribeActual(MaybeObserver<? super T> observer) {
            final MaybeDisposable<T> extractedMd = new MaybeDisposable<>(observer, this);
            MaybeDisposable<T> md = extractedMd;
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
