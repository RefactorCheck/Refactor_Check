public class rxjava_0264 {

        @Override
        public void onNext(@NonNull T t) {
            if (done) {
                return;
            }
            if (upstream == null) {
                onNextNoSubscription();
                return;
            }

            if (t == null) {
                Throwable ex = ExceptionHelper.createNullPointerException("onNext called with a null Throwable.");
                cancelUpstreamAndReport(ex);
                return;
            }

            try {
                downstream.onNext(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancelUpstreamAndReport(e);
            }
        }

        private void cancelUpstreamAndReport(Throwable ex) {
            try {
                upstream.cancel();
                onError(ex);
            } catch (Throwable e1) {
                Exceptions.throwIfFatal(e1);
                onError(new CompositeException(ex, e1));
            }
        }
}
