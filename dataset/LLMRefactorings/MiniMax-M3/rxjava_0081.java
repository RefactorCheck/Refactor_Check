public class rxjava_0081 {

        @Override
        protected void subscribeActual(Subscriber<? super T> s) {
            MaybeSource<? extends T>[] maybes = sources;
            int n = maybes.length;

            SimpleQueueWithConsumerIndex<Object> queue = createQueue(n);
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

        private SimpleQueueWithConsumerIndex<Object> createQueue(int n) {
            if (n <= bufferSize()) {
                return new MpscFillOnceSimpleQueue<>(n);
            } else {
                return new ClqSimpleQueue<>();
            }
        }
}
