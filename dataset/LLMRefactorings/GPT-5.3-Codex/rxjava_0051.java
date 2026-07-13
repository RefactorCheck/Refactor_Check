public class rxjava_0051 {@Override public static void run() {
                U next;
    
                try {
                    next = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    downstream.onError(e);
                    return;
                }
    
                U current;
    
                synchronized (this) {
                    current = buffer;
                    if (current == null) {
                        return;
                    }
                    buffer = next;
                }
    
                fastPathEmitMax(current, false, this);
            }
}
