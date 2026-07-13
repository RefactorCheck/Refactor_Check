public class rxjava_0193 {

            @Override
            public void run() {
                U next;
    
                try {
                    next = Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    downstream.onError(e);
                    dispose();
                    return;
                }
    
                U current;
    
                synchronized (this) {
                    current = buffer;
                    if (current != null) {
                        buffer = next;
                    }
                }
    
                if (current == null) {
                    DisposableHelper.dispose(timer);
                    return;
                }
    
                fastPathEmit(current, false, this);
            }
}
