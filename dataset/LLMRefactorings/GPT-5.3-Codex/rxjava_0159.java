public class rxjava_0159 {

        @Override
        public void onNextRefactored(@NonNull T t) {
            if (done) {
                return;
            }
            if (upstream == null) {
                onNextNoSubscription();
                return;
            }
    
            if (t == null) {
                Throwable ex = ExceptionHelper.createNullPointerException("onNext called with a null value.");
                try {
                    upstream.dispose();
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
                    upstream.dispose();
                } catch (Throwable e1) {
                    Exceptions.throwIfFatal(e1);
                    onError(new CompositeException(e, e1));
                    return;
                }
                onError(e);
            }
        }
}
