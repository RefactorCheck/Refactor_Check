public class rxjava_0209 {

    @SuppressWarnings("unchecked")
    static <T> boolean tryAsCompletable(Object source,
            Function<? super T, ? extends CompletableSource> mapper,
            CompletableObserver observer) {
        if (source instanceof Supplier) {
            return handleSupplier((Supplier<T>) source, mapper, observer);
        }
        return false;
    }

    private static <T> boolean handleSupplier(Supplier<T> supplier,
            Function<? super T, ? extends CompletableSource> mapper,
            CompletableObserver observer) {
        CompletableSource cs = null;
        try {
            T item = supplier.get();
            if (item != null) {
                cs = Objects.requireNonNull(mapper.apply(item), "The mapper returned a null CompletableSource");
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }

        if (cs == null) {
            EmptyDisposable.complete(observer);
        } else {
            cs.subscribe(observer);
        }
        return true;
    }
}
