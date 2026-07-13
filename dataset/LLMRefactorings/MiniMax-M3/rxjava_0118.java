public class rxjava_0118 {

        private static final String NULL_SOURCE_ERROR_MESSAGE = "One of the sources is null";

        @Override
        protected void subscribeActual(MaybeObserver<? super R> observer) {
            MaybeSource<? extends T>[] sources = this.sources;
            int n = sources.length;
    
            if (n == 1) {
                sources[0].subscribe(new MaybeMap.MapMaybeObserver<>(observer, new SingletonArrayFunc()));
                return;
            }
    
            ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, zipper);
    
            observer.onSubscribe(parent);
    
            for (int i = 0; i < n; i++) {
                if (parent.isDisposed()) {
                    return;
                }
    
                MaybeSource<? extends T> source = sources[i];
    
                if (source == null) {
                    parent.innerError(new NullPointerException(NULL_SOURCE_ERROR_MESSAGE), i);
                    return;
                }
                source.subscribe(parent.observers[i]);
            }
        }
}
