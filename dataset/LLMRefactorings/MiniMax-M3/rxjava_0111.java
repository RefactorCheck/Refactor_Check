public class rxjava_0111 {

    private static final int INITIAL_CAPACITY = 8;

    @Override
    @SuppressWarnings("unchecked")
    public void subscribeActual(Observer<? super R> observer) {
        ObservableSource<? extends T>[] sources = this.sources;
        int count = 0;
        if (sources == null) {
            sources = new ObservableSource[INITIAL_CAPACITY];
            for (ObservableSource<? extends T> p : sourcesIterable) {
                if (count == sources.length) {
                    ObservableSource<? extends T>[] b = new ObservableSource[count + (count >> 2)];
                    System.arraycopy(sources, 0, b, 0, count);
                    sources = b;
                }
                sources[count++] = p;
            }
        } else {
            count = sources.length;
        }

        if (count == 0) {
            EmptyDisposable.complete(observer);
            return;
        }

        ZipCoordinator<T, R> zc = new ZipCoordinator<>(observer, zipper, count, delayError);
        zc.subscribe(sources, bufferSize);
    }
}
