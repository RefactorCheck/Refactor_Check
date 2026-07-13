public class rxjava_0166 {

            @Override
            public void onNext(T t) {
                if (!done) {
                    try {
                        safeAccept(parent.onNext, t);
                    } catch (Throwable ex) {
                        onError(ex);
                        return;
                    }
    
                    downstream.onNext(t);
    
                    try {
                        safeAccept(parent.onAfterNext, t);
                    } catch (Throwable ex) {
                        onError(ex);
                    }
                }
            }

            private void safeAccept(Consumer<T> consumer, T value) throws Throwable {
                try {
                    consumer.accept(value);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    throw ex;
                }
            }
}
