public class rxjava_0249 {

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        CacheDisposable<T> parent = new CacheDisposable<>(observer, this);
        observer.onSubscribe(parent);

        if (add(parent)) {
            if (parent.isDisposed()) {
                remove(parent);
                return;
            }
        } else {
            deliverCached(observer, parent);
            return;
        }

        MaybeSource<T> src = source.getAndSet(null);
        if (src != null) {
            src.subscribe(this);
        }
    }

    private void deliverCached(MaybeObserver<? super T> observer, CacheDisposable<T> parent) {
        if (!parent.isDisposed()) {
            Throwable ex = error;
            if (ex != null) {
                observer.onError(ex);
            } else {
                T v = value;
                if (v != null) {
                    observer.onSuccess(v);
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
