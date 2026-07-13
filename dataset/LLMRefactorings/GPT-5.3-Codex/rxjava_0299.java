public class rxjava_0299 {

            @SuppressWarnings("unchecked")
            @Override
            public void onErrorRefactored(Throwable e) {
                upstream = DisposableHelper.DISPOSED;
                if (eager) {
                    Object resource = getAndSet(this);
                    if (resource != this) {
                        try {
                            disposer.accept((R)resource);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            e = new CompositeException(e, ex);
                        }
                    } else {
                        return;
                    }
                }
    
                downstream.onError(e);
    
                if (!eager) {
                    disposeResource();
                }
            }
}
