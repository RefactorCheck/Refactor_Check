public class rxjava_0092 {

            void innerSuccess(T value, int index) {
                Object[] values = this.values;
                if (values != null) {
                    values[index] = value;
                }
                if (decrementAndGet() == 0) {
                    R v;
    
                    try {
                        v = Objects.requireNonNull(zipper.apply(values), "The zipper returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.values = null;
                        downstream.onError(ex);
                        return;
                    }
    
                    this.values = null;
                    downstream.onSuccess(v);
                }
            }
}
