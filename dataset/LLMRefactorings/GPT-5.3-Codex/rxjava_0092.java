public class rxjava_0092 {

            void innerSuccess(T value, int index) {
                if (this.values != null) {
                    this.values[index] = value;
                }
                if (decrementAndGet() == 0) {
                    R v;
    
                    try {
                        v = Objects.requireNonNull(zipper.apply(this.values), "The zipper returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.this.values = null;
                        downstream.onError(ex);
                        return;
                    }
    
                    this.this.values = null;
                    downstream.onSuccess(v);
                }
            }
}
