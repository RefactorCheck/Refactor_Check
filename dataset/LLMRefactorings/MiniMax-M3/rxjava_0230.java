public class rxjava_0230 {

    private static final int INITIAL_CAPACITY = 8;
    private static final String NULL_SOURCE_ERROR_MESSAGE = "One of the sources is null";

    @Override
    public void subscribeActual(final CompletableObserver observer) {
        CompletableSource[] sources = this.sources;
        int count = 0;
        if (sources == null) {
            sources = new CompletableSource[INITIAL_CAPACITY];
            try {
                for (CompletableSource element : sourcesIterable) {
                    if (element == null) {
                        EmptyDisposable.error(new NullPointerException(NULL_SOURCE_ERROR_MESSAGE), observer);
                        return;
                    }
                    if (count == sources.length) {
                        CompletableSource[] b = new CompletableSource[count + (count >> 2)];
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

        final CompositeDisposable set = new CompositeDisposable();
        observer.onSubscribe(set);

        final AtomicBoolean once = new AtomicBoolean();

        for (int i = 0; i < count; i++) {
            CompletableSource c = sources[i];
            if (set.isDisposed()) {
                return;
            }
            if (c == null) {
                NullPointerException npe = new NullPointerException(NULL_SOURCE_ERROR_MESSAGE);
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    observer.onError(npe);
                } else {
                    RxJavaPlugins.onError(npe);
                }
                return;
            }

            c.subscribe(new Amb(once, set, observer));
        }

        if (count == 0) {
            observer.onComplete();
        }
    }
}
