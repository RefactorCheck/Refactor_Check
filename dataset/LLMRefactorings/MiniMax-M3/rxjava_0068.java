public class rxjava_0068 {

    @Override
    public void run() {
        if (cancelled) {
            return;
        }
        final U b;

        try {
            b = getBuffer();
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

    private U getBuffer() {
        return Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
    }
}
