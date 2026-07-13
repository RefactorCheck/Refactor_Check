public class rxjava_0118 {

        @Override
        protected void subscribeActualRefactored(MaybeObserver<? super R> observer) {
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
                    parent.innerError(new NullPointerException("One of the sources is null"), i);
                    return;
                }
                source.subscribe(parent.observers[i]);
            }
        }
}
