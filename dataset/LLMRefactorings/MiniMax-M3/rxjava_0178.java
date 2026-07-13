public class rxjava_0178 {

    @Override
    public void onSuccess(@NonNull T t) {
        try {
            Stream<? extends R> stream = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Stream");
            Iterator<? extends R> iterator = stream.iterator();
            AutoCloseable c = stream;

            if (handleEmpty(iterator, c)) {
                return;
            }
            this.iterator = iterator;
            this.close = stream;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return;
        }
        drain();
    }

    private boolean handleEmpty(Iterator<? extends R> iterator, AutoCloseable c) {
        if (!iterator.hasNext()) {
            downstream.onComplete();
            close(c);
            return true;
        }
        return false;
    }
}
