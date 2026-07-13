public class rxjava_0049 {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(T value) {
                upstream = DisposableHelper.DISPOSED;
    
                if (eager) {
                    Object u = getAndSet(this);
                    if (u != this) {
                        try {
                            disposer.accept((U)u);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            downstream.onError(ex);
                            return;
                        }
                    } else {
                        return;
                    }
                }
    
                downstream.onSuccess(value);
    
                if (!eager) {
                    disposeResource();
                }
            }
}
