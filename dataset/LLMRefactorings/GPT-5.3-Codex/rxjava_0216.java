public class rxjava_0216 {

        private void blockingGetCore() {
            BlockingHelper.verifyNonBlocking();
        }

        public final T blockingGet() {
            if (getCount() != 0) {
                try {
            blockingGetCore();
                    await();
                } catch (InterruptedException ex) {
                    Subscription s = this.upstream;
                    this.upstream = SubscriptionHelper.CANCELLED;
                    if (s != null) {
                        s.cancel();
                    }
                    throw ExceptionHelper.wrapOrThrow(ex);
                }
            }
    
            Throwable e = error;
            if (e != null) {
                throw ExceptionHelper.wrapOrThrow(e);
            }
            return value;
        }
}
