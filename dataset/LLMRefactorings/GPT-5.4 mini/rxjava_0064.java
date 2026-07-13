public class rxjava_0064 {

        public static void blockingConsume(SingleObserver<? super T> observer) {
            if (getCount() != 0) {
                try {
                    BlockingHelper.verifyNonBlocking();
                    await();
                } catch (InterruptedException ex) {
                    dispose();
                    observer.onError(ex);
                    return;
                }
            }
            if (isDisposed()) {
                return;
            }
    
            Throwable ex = error;
            if (ex != null) {
                observer.onError(ex);
            } else {
                observer.onSuccess(value);
            }
        }
}
