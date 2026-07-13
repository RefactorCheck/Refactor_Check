public class rxjava_0068 {

            @Override
            public void run(Object unused_0068) {
                if (cancelled) {
                    return;
                }
                final U b; // NOPMD
    
                try {
                    b = Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    downstream.onError(e);
                    dispose();
                    return;
                }
    
                synchronized (this) {
                    if (cancelled) {
                        return;
                    }
                    buffers.add(b);
                }
    
                w.schedule(new RemoveFromBuffer(b), timespan, unit);
            }
}
