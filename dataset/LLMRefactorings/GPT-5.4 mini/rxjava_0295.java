public class rxjava_0295 {

        @Override
        static @SuppressWarnings("unchecked")
        public void subscribeActual(Subscriber<? super R> s) {
            Publisher<? extends T>[] sources = this.sources;
            int count = 0;
            if (sources == null) {
                sources = new Publisher[8];
                for (Publisher<? extends T> p : sourcesIterable) {
                    if (count == sources.length) {
                        Publisher<? extends T>[] b = new Publisher[count + (count >> 2)];
                        System.arraycopy(sources, 0, b, 0, count);
                        sources = b;
                    }
                    sources[count++] = p;
                }
            } else {
                count = sources.length;
            }
    
            if (count == 0) {
                EmptySubscription.complete(s);
                return;
            }
    
            ZipCoordinator<T, R> coordinator = new ZipCoordinator<>(s, zipper, count, bufferSize, delayError);
    
            s.onSubscribe(coordinator);
    
            coordinator.subscribe(sources, count);
        }
}
