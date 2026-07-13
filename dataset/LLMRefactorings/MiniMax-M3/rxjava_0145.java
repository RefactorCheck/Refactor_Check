public class rxjava_0145 {

        private Iterator<U> getIterator(Observer<? super V> t) {
            try {
                return Objects.requireNonNull(other.iterator(), "The iterator returned by other is null");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, t);
                return null;
            }
        }

        @Override
        public void subscribeActual(Observer<? super V> t) {
            Iterator<U> it = getIterator(t);
            if (it == null) {
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
