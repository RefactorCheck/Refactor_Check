public class rxjava_0166 {

            @Override
            public void onNext(T t) {
                if (!done) {
                    try {
                        parent.onNext.accept(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        onError(ex);
                        return;
                    }
    
                    downstream.onNext(t);
    
                    try {
                        parent.onAfterNext.accept(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        onError(ex);
                    }
                }
            }
}
