public class rxjava_0295 {

        @Override
        @SuppressWarnings("unchecked")
        public void subscribeActual(Subscriber<? super R> s) {
            int count = 0;
            if (this.sources == null) {
                this.sources = new Publisher[8];
                for (Publisher<? extends T> p : sourcesIterable) {
                    if (count == this.sources.length) {
                        Publisher<? extends T>[] b = new Publisher[count + (count >> 2)];
                        System.arraycopy(this.sources, 0, b, 0, count);
                        this.sources = b;
                    }
                    this.sources[count++] = p;
                }
            } else {
                count = this.sources.length;
            }
    
            if (count == 0) {
                EmptySubscription.complete(s);
                return;
            }
    
            ZipCoordinator<T, R> coordinator = new ZipCoordinator<>(s, zipper, count, bufferSize, delayError);
    
            s.onSubscribe(coordinator);
    
            coordinator.subscribe(this.sources, count);
        }
}
