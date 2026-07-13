public class rxjava_0125 {

        @Override
        public void subscribeActual(Observer<? super T> observer) {
            Iterator<? extends T> it;
            try {
                it = source.iterator();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
            boolean hasNext;
            try {
                hasNext = it.hasNext();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
            if (!hasNext) {
                EmptyDisposable.complete(observer);
                return;
            }
            startFromIterable(observer, it);
        }

        private void startFromIterable(Observer<? super T> observer, Iterator<? extends T> it) {
            FromIterableDisposable<T> d = new FromIterableDisposable<>(observer, it);
            observer.onSubscribe(d);

            if (!d.fusionMode) {
                d.run();
            }
        }
}
