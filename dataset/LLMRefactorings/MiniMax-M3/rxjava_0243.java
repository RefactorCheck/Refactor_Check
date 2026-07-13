public class rxjava_0243 {

        public static void onError(@NonNull Throwable error) {
            Consumer<? super Throwable> f = errorHandler;
            error = prepareError(error);
    
            if (f != null) {
                try {
                    f.accept(error);
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
    
        private static Throwable prepareError(Throwable error) {
            if (error == null) {
                return ExceptionHelper.createNullPointerException("onError called with a null Throwable.");
            }
            if (!isBug(error)) {
                return new UndeliverableException(error);
            }
            return error;
        }
}
