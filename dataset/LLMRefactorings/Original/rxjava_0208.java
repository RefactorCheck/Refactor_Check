public class rxjava_0208 {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(T value) {
                upstream = DisposableHelper.DISPOSED;
                if (eager) {
                    Object resource = getAndSet(this);
                    if (resource != this) {
                        try {
                            disposer.accept((D)resource);
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
