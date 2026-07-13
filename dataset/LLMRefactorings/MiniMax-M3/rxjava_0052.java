public class rxjava_0052 {

    @Override
    public void run() {
        if (cancelled) {
            return;
        }
        final U b; // NOPMD

        try {
            b = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            cancel();
            downstream.onError(e);
            return;
        }

        addBuffer(b);
        w.schedule(new RemoveFromBuffer(b), timespan, unit);
    }

    private void addBuffer(U b) {
        synchronized (this) {
            if (cancelled) {
                return;
            }
            buffers.add(b);
        }
    }
}
