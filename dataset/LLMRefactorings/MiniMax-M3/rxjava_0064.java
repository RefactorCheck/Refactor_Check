public class rxjava_0064 {

        public void blockingConsume(SingleObserver<? super T> observer) {
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

            deliverResult(observer);
        }

        private void deliverResult(SingleObserver<? super T> observer) {
            Throwable ex = error;
            if (ex != null) {
                observer.onError(ex);
            } else {
                observer.onSuccess(value);
            }
        }
}
