public class rxjava_0266 {

    @SuppressWarnings("unchecked")
    public static <T, R> boolean tryScalarXMapSubscribe(ObservableSource<T> source,
            Observer<? super R> observer,
            Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        if (source instanceof Supplier) {
            T t = tryScalar((Supplier<T>) source, observer);
            if (t == null) {
                return true;
            }

            ObservableSource<? extends R> r = tryScalar(
                () -> Objects.requireNonNull(mapper.apply(t), "The mapper returned a null ObservableSource"),
                observer
            );
            if (r == null) {
                return true;
            }

            if (r instanceof Supplier) {
                R u = tryScalar((Supplier<R>) r, observer);
                if (u == null) {
                    return true;
                }
                ScalarDisposable<R> sd = new ScalarDisposable<>(observer, u);
                observer.onSubscribe(sd);
                sd.run();
            } else {
                r.subscribe(observer);
            }

            return true;
        }
        return false;
    }

    private static <T> T tryScalar(Supplier<? extends T> supplier, Observer<?> observer) {
        T value;
        try {
            value = supplier.get();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return null;
        }
        if (value == null) {
            EmptyDisposable.complete(observer);
            return null;
        }
        return value;
    }
}
