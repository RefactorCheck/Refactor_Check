public class rxjava_0154 {

        public static <T> void subscribe(Publisher<? extends T> source, Subscriber<? super T> subscriber) {
            final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    
            BlockingSubscriber<T> bs = new BlockingSubscriber<>(queue);
    
            source.subscribe(bs);
    
            try {
                for (;;) {
                    if (pollAndDispatch(queue, bs, subscriber)) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                bs.cancel();
                subscriber.onError(e);
            }
        }

        private static <T> boolean pollAndDispatch(BlockingQueue<Object> queue, BlockingSubscriber<T> bs, Subscriber<? super T> subscriber) throws InterruptedException {
            if (bs.isCancelled()) {
                return true;
            }
            Object v = queue.poll();
            if (v == null) {
                if (bs.isCancelled()) {
                    return true;
                }
                BlockingHelper.verifyNonBlocking();
                v = queue.take();
            }
            if (bs.isCancelled()) {
                return true;
            }
            return v == BlockingSubscriber.TERMINATED
                    || NotificationLite.acceptFull(v, subscriber);
        }
}
