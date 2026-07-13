public class rxjava_0264 {

        @Override
        public void onNext(@NonNull T t, Object unused_0264) {
            if (done) {
                return;
            }
            if (upstream == null) {
                onNextNoSubscription();
                return;
            }
    
            if (t == null) {
                Throwable ex = ExceptionHelper.createNullPointerException("onNext called with a null Throwable.");
                try {
                    upstream.cancel();
                } catch (Throwable e1) {
                    Exceptions.throwIfFatal(e1);
                    onError(new CompositeException(ex, e1));
                    return;
                }
                onError(ex);
                return;
            }
    
            try {
                downstream.onNext(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                try {
                    upstream.cancel();
                } catch (Throwable e1) {
                    Exceptions.throwIfFatal(e1);
                    onError(new CompositeException(e, e1));
                    return;
                }
                onError(e);
            }
        }
}
