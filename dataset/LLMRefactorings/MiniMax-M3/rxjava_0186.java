public class rxjava_0186 {

        @Override
        protected void subscribeActual(MaybeObserver<? super R> observer) {
            @SuppressWarnings("unchecked")
            MaybeSource<? extends T>[] a = new MaybeSource[8];
            int n = 0;
    
            try {
                for (MaybeSource<? extends T> source : sources) {
                    if (source == null) {
                        EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
                        return;
                    }
                    if (n == a.length) {
                        a = grow(a, n);
                    }
                    a[n++] = source;
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptyDisposable.error(ex, observer);
                return;
            }
    
            if (n == 0) {
                EmptyDisposable.complete(observer);
                return;
            }
    
            if (n == 1) {
                a[0].subscribe(new MaybeMap.MapMaybeObserver<>(observer, new SingletonArrayFunc()));
                return;
            }
    
            ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, zipper);
    
            observer.onSubscribe(parent);
    
            for (int i = 0; i < n; i++) {
                if (parent.isDisposed()) {
                    return;
                }
    
                a[i].subscribe(parent.observers[i]);
            }
        }

        private static <T> T[] grow(T[] array, int oldCapacity) {
            return Arrays.copyOf(array, oldCapacity + (oldCapacity >> 2));
        }
}
