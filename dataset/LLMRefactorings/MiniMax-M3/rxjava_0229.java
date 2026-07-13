public class rxjava_0229 {

    static <T, R> boolean tryAsMaybe(Object source,
            Function<? super T, ? extends MaybeSource<? extends R>> mapper,
            Observer<? super R> observer) {
        if (source instanceof Supplier) {
            @SuppressWarnings("unchecked")
            Supplier<T> supplier = (Supplier<T>) source;
            MaybeSource<? extends R> cs = null;
            try {
                T item = supplier.get();
                if (item != null) {
                    cs = Objects.requireNonNull(mapper.apply(item), "The mapper returned a null MaybeSource");
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptyDisposable.error(ex, observer);
                return true;
            }

            subscribeToMaybeSource(cs, observer);
            return true;
        }
        return false;
    }

    private static <R> void subscribeToMaybeSource(MaybeSource<? extends R> cs, Observer<? super R> observer) {
        if (cs == null) {
            EmptyDisposable.complete(observer);
        } else {
            cs.subscribe(MaybeToObservable.create(observer));
        }
    }
}
