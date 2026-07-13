public class rxjava_0067 {

        @Override
        protected void subscribeActual(SingleObserver<? super R> observer) {
            SingleSource<? extends T>[] sources = this.sources;
            int n = sources.length;
    
            if (n == 1) {
                sources[0].subscribe(new SingleMap.MapSingleObserver<>(observer, new SingletonArrayFunc()));
                return;
            }
    
            ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, zipper);
    
            observer.onSubscribe(parent);
    
            for (int i = 0; i < n; i++) {
                if (parent.isDisposed()) {
                    return;
                }
    
                SingleSource<? extends T> source = sources[i];
    
                if (source == null) {
                    parent.innerError(new NullPointerException("One of the sources is null"), i);
                    return;
                }
    
                source.subscribe(parent.observers[i]);
            }
        }
}
