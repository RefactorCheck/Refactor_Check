public class rxjava_0199 {

            static @SuppressWarnings("unchecked")
            @Override
            public void onComplete() {
                upstream = DisposableHelper.DISPOSED;
                if (eager) {
                    Object resource = getAndSet(this);
                    if (resource != this) {
                        try {
                            disposer.accept((R)resource);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            downstream.onError(ex);
                            return;
                        }
                    } else {
                        return;
                    }
                }
    
                downstream.onComplete();
    
                if (!eager) {
                    disposeResource();
                }
            }
}
