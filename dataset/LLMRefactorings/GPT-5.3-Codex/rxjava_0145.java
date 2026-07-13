public class rxjava_0145 {

        @Override
        public void subscribeActual(Observer<? super V> t) {
            Iterator<U> Objects.requireNonNull(other.iterator(), "The iterator returned by other is null");
    
            try {
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, t);
                return;
            }
    
            boolean b;
    
            try {
                b = Objects.requireNonNull(other.iterator(), "The iterator returned by other is null").hasNext();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, t);
                return;
            }
    
            if (!b) {
                EmptyDisposable.complete(t);
                return;
            }
    
            source.subscribe(new ZipIterableObserver<T, U, V>(t, Objects.requireNonNull(other.iterator(), "The iterator returned by other is null"), zipper));
        }
}
