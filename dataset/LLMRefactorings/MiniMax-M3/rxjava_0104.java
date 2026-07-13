public class rxjava_0104 {

    @Override
    protected void subscribeActual(SingleObserver<? super R> observer) {
        @SuppressWarnings("unchecked")
        SingleSource<? extends T>[] a = new SingleSource[8];
        int n = 0;

        try {
            for (SingleSource<? extends T> source : sources) {
                if (source == null) {
                    EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
                    return;
                }
                if (n == a.length) {
                    a = Arrays.copyOf(a, n + (n >> 2));
                }
                a[n++] = source;
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return;
        }

        if (n == 0) {
            EmptyDisposable.error(new NoSuchElementException(), observer);
            return;
        }

        if (n == 1) {
            a[0].subscribe(new SingleMap.MapSingleObserver<>(observer, new SingletonArrayFunc()));
            return;
        }

        ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, zipper);

        observer.onSubscribe(parent);

        subscribeToAll(a, n, parent);
    }

    private void subscribeToAll(SingleSource<? extends T>[] a, int n, ZipCoordinator<T, R> parent) {
        for (int i = 0; i < n; i++) {
            if (parent.isDisposed()) {
                return;
            }

            a[i].subscribe(parent.observers[i]);
        }
    }
}
