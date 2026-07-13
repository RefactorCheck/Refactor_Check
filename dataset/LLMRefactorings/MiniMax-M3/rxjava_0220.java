public class rxjava_0220 {

    private static final String NULL_BUFFER_MESSAGE = "The buffer supplied is null";

    @Override
    public void onSubscribe(Disposable d) {
        if (DisposableHelper.validate(this.upstream, d)) {
            this.upstream = d;

            U b;

            try {
                b = Objects.requireNonNull(bufferSupplier.get(), NULL_BUFFER_MESSAGE);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancelled = true;
                d.dispose();
                EmptyDisposable.error(e, downstream);
                return;
            }

            buffer = b;

            BufferBoundaryObserver<T, U, B> bs = new BufferBoundaryObserver<>(this);
            other = bs;

            downstream.onSubscribe(this);

            if (!cancelled) {
                boundary.subscribe(bs);
            }
        }
    }
}
