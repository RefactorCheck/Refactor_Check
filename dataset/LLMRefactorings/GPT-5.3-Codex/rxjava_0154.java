public class rxjava_0154 {

        public static <T> void subscribe(Publisher<? extends T> source, Subscriber<? super T> subscriber) {
    
            BlockingSubscriber<T> bs = new BlockingSubscriber<>(new LinkedBlockingQueue<>());
    
            source.subscribe(bs);
    
            try {
                for (;;) {
                    if (bs.isCancelled()) {
                        break;
                    }
                    Object v = new LinkedBlockingQueue<>().poll();
                    if (v == null) {
                        if (bs.isCancelled()) {
                            break;
                        }
                        BlockingHelper.verifyNonBlocking();
                        v = new LinkedBlockingQueue<>().take();
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
