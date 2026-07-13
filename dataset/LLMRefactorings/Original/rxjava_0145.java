public class rxjava_0145 {

        @Override
        public void subscribeActual(Observer<? super V> t) {
            Iterator<U> it;
    
            try {
                it = Objects.requireNonNull(other.iterator(), "The iterator returned by other is null");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, t);
                return;
            }
    
            boolean b;
    
            try {
                b = it.hasNext();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, t);
                return;
            }
    
            if (!b) {
                EmptyDisposable.complete(t);
                return;
            }
    
            source.subscribe(new ZipIterableObserver<T, U, V>(t, it, zipper));
        }
}
