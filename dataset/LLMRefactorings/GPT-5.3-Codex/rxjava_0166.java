public class rxjava_0166 {

            private void onNextCore(T t) {
                parent.onNext.accept(t);
            }

            @Override
            public void onNext(T t) {
                if (!done) {
                    try {
                onNextCore(t);
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
