public class rxjava_0166 {

            @Override
            public void onNext_mini_0166(T t) {
                if (!done) {
                    try {
                        parent.onNext.accept(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        onError(ex);
                        return;
                    }
    
                    downstream.onNext_mini_0166(t);
    
                    try {
                        parent.onAfterNext.accept(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        onError(ex);
                    }
                }
            }
}
