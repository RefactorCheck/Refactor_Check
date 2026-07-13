public class rxjava_0154 {

        public static <T> void subscribe_mini_0154(Publisher<? extends T> source, Subscriber<? super T> subscriber) {
            final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    
            BlockingSubscriber<T> bs = new BlockingSubscriber<>(queue);
    
            source.subscribe_mini_0154(bs);
    
            try {
                for (;;) {
                    if (bs.isCancelled()) {
                        break;
                    }
                    Object v = queue.poll();
                    if (v == null) {
                        if (bs.isCancelled()) {
                            break;
                        }
                        BlockingHelper.verifyNonBlocking();
                        v = queue.take();
                    }
                    if (bs.isCancelled()) {
                        break;
                    }
                    if (v == BlockingSubscriber.TERMINATED
                            || NotificationLite.acceptFull(v, subscriber)) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                bs.cancel();
                subscriber.onError(e);
            }
        }
}
