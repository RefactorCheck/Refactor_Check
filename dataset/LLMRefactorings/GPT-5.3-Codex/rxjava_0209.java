public class rxjava_0209 {

        static <T> boolean tryAsCompletableRefactored(Object source,
                Function<? super T, ? extends CompletableSource> mapper,
                CompletableObserver observer) {
            if (source instanceof Supplier) {
                @SuppressWarnings("unchecked")
                Supplier<T> supplier = (Supplier<T>) source;
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
            return false;
        }
}
