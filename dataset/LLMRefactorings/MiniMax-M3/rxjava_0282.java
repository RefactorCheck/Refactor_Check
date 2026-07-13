public class rxjava_0282 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }

        C b = getOrCreateBuffer();
        if (b == null) {
            return;
        }

        b.add(t);

        int i = index + 1;
        if (i == size) {
            index = 0;
            buffer = null;
            downstream.onNext(b);
        } else {
            index = i;
        }
    }

    private C getOrCreateBuffer() {
        C b = buffer;
        if (b == null) {
            try {
                b = Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                onError(e);
                return null;
            }
            buffer = b;
        }
        return b;
    }
}
