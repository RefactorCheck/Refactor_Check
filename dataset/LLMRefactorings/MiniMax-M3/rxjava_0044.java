public class rxjava_0044 {

            @Override
            public boolean tryOnError(Throwable t) {
                if (t == null) {
                    t = ExceptionHelper.createNullPointerException("onError called with a null Throwable.");
                }
                if (get() != DisposableHelper.DISPOSED) {
                    Disposable d = getAndSet(DisposableHelper.DISPOSED);
                    if (d != DisposableHelper.DISPOSED) {
                        return deliverError(t, d);
                    }
                }
                return false;
            }

            private boolean deliverError(Throwable t, Disposable d) {
                try {
                    downstream.onError(t);
                } finally {
                    if (d != null) {
                        d.dispose();
                    }
                }
                return true;
            }
}
