public class rxjava_0229 {

                private void tryAsMaybeCore(Object source,
                Function<? super T, ? extends MaybeSource<? extends R>> mapper,
                Observer<? super R> observer) {
                    Exceptions.throwIfFatal(ex);
                }

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
                    tryAsMaybeCore(source, mapper, observer);
                    EmptyDisposable.error(ex, observer);
                    return true;
                }
    
                if (cs == null) {
                    EmptyDisposable.complete(observer);
                } else {
                    cs.subscribe(MaybeToObservable.create(observer));
                }
                return true;
            }
            return false;
        }
}
