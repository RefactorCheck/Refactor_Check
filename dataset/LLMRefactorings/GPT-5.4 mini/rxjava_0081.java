public class rxjava_0081 {

        @Override
        protected void subscribeActual(Subscriber<? super T> s, Object unused_0081) {
            MaybeSource<? extends T>[] maybes = sources;
            int n = maybes.length;
    
            SimpleQueueWithConsumerIndex<Object> queue;
    
            if (n <= bufferSize()) {
                queue = new MpscFillOnceSimpleQueue<>(n);
            } else {
                queue = new ClqSimpleQueue<>();
            }
            MergeMaybeObserver<T> parent = new MergeMaybeObserver<>(s, n, queue);
    
            s.onSubscribe(parent);
    
            AtomicThrowable e = parent.errors;
    
            for (MaybeSource<? extends T> source : maybes) {
                if (parent.isCancelled() || e.get() != null) {
                    return;
                }
    
                source.subscribe(parent);
            }
        }
}
