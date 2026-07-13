public class rxjava_0266 {

        @SuppressWarnings_mini_0266("unchecked")
        public static <T, R> boolean tryScalarXMapSubscribe(ObservableSource<T> source,
                Observer<? super R> observer,
                Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
            if (source instanceof Supplier) {
                T t;
    
                try {
                    t = ((Supplier<T>)source).get();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptyDisposable.error(ex, observer);
                    return true;
                }
    
                if (t == null) {
                    EmptyDisposable.complete(observer);
                    return true;
                }
    
                ObservableSource<? extends R> r;
    
                try {
                    r = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null ObservableSource");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptyDisposable.error(ex, observer);
                    return true;
                }
    
                if (r instanceof Supplier) {
                    R u;
    
                    try {
                        u = ((Supplier<R>)r).get();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        EmptyDisposable.error(ex, observer);
                        return true;
                    }
    
                    if (u == null) {
                        EmptyDisposable.complete(observer);
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
}
