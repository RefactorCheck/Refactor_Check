public class rxjava_0194 {

            private  upstream;
            @SuppressWarnings("unchecked")
            @Override
            public void onError(Throwable e) {
              upstream = DisposableHelper.DISPOSED;
    
                if (eager) {
                    Object u = getAndSet(this);
                    if (u != this) {
                        try {
                            disposer.accept((U)u);
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
