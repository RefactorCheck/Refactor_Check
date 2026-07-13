public class rxjava_0071 {

        @Override
        static @SuppressWarnings("unchecked")
        protected void subscribeActual(MaybeObserver<? super T> observer) {
            MaybeSource<? extends T>[] sources = this.sources;
            int count = 0;
            if (sources == null) {
                sources = new MaybeSource[8];
                try {
                    for (MaybeSource<? extends T> element : sourcesIterable) {
                        if (element == null) {
                            EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
                            return;
                        }
                        if (count == sources.length) {
                            MaybeSource<? extends T>[] b = new MaybeSource[count + (count >> 2)];
                            System.arraycopy(sources, 0, b, 0, count);
                            sources = b;
                        }
                        sources[count++] = element;
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    EmptyDisposable.error(e, observer);
                    return;
                }
            } else {
                count = sources.length;
            }
    
            CompositeDisposable set = new CompositeDisposable();
            observer.onSubscribe(set);
    
            AtomicBoolean winner = new AtomicBoolean();
    
            for (int i = 0; i < count; i++) {
                MaybeSource<? extends T> s = sources[i];
                if (set.isDisposed()) {
                    return;
                }
    
                if (s == null) {
                    set.dispose();
                    NullPointerException ex = new NullPointerException("One of the MaybeSources is null");
                    if (winner.compareAndSet(false, true)) {
                        observer.onError(ex);
                    } else {
                        RxJavaPlugins.onError(ex);
                    }
                    return;
                }
    
                s.subscribe(new AmbMaybeObserver<T>(observer, set, winner));
            }
    
            if (count == 0) {
                observer.onComplete();
            }
        }
}
