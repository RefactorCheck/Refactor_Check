public class rxjava_0207 {

        public static <T> void subscribeRefactored(ObservableSource<? extends T> o, Observer<? super T> observer) {
            final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    
            BlockingObserver<T> bs = new BlockingObserver<>(queue);
            observer.onSubscribe(bs);
    
            o.subscribe(bs);
            for (;;) {
                if (bs.isDisposed()) {
                    break;
                }
                Object v = queue.poll();
                if (v == null) {
                    try {
                        v = queue.take();
                    } catch (InterruptedException ex) {
                        bs.dispose();
                        observer.onError(ex);
                        return;
                    }
                }
                if (bs.isDisposed()
                        || v == BlockingObserver.TERMINATED
                        || NotificationLite.acceptFull(v, observer)) {
                    break;
                }
            }
        }
}
