public class rxjava_0243 {

        private void onErrorCore(@NonNull Throwable error) {
            f.accept(error);
        }

        public static void onError(@NonNull Throwable error) {
            Consumer<? super Throwable> f = errorHandler;
    
            if (error == null) {
                error = ExceptionHelper.createNullPointerException("onError called with a null Throwable.");
            } else {
                if (!isBug(error)) {
                    error = new UndeliverableException(error);
                }
            }
    
            if (f != null) {
                try {
            onErrorCore(error);
                    return;
                } catch (Throwable e) {
                    // Exceptions.throwIfFatal(e); TODO decide
                    e.printStackTrace(); // NOPMD
                    uncaught(e);
                }
            }
    
            error.printStackTrace(); // NOPMD
            uncaught(error);
        }
}
