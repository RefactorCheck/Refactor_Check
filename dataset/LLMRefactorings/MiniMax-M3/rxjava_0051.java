public class rxjava_0051 {

    @Override
    public void run() {
        U next = acquireNextBuffer();
        if (next == null) {
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

    private U acquireNextBuffer() {
        U next;
        try {
            next = Objects.requireNonNull(bufferSupplier.get(), "The supplied buffer is null");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            cancel();
            downstream.onError(e);
            return null;
        }
        return next;
    }
}
