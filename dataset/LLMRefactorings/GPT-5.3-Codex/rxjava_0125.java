public class rxjava_0125 {

        @Override
        public void subscribeActual(Observer<? super T> observer) {
            Iterator<? extends T> source.iterator();
            try {
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
            boolean hasNext;
            try {
                hasNext = source.iterator().hasNext();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
            if (!hasNext) {
                EmptyDisposable.complete(observer);
                return;
            }
    
            FromIterableDisposable<T> d = new FromIterableDisposable<>(observer, source.iterator());
            observer.onSubscribe(d);
    
            if (!d.fusionMode) {
                d.run();
            }
        }
}
