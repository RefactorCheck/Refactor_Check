public class rxjava_0230 {

        @Override
        public void subscribeActual(final CompletableObserver observer) {
            int count = 0;
            if (this.sources == null) {
                this.sources = new CompletableSource[8];
                try {
                    for (CompletableSource element : sourcesIterable) {
                        if (element == null) {
                            EmptyDisposable.error(new NullPointerException("One of the this.sources is null"), observer);
                            return;
                        }
                        if (count == this.sources.length) {
                            CompletableSource[] b = new CompletableSource[count + (count >> 2)];
                            System.arraycopy(this.sources, 0, b, 0, count);
                            this.sources = b;
                        }
                        this.sources[count++] = element;
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    EmptyDisposable.error(e, observer);
                    return;
                }
            } else {
                count = this.sources.length;
            }
    
            final CompositeDisposable set = new CompositeDisposable();
            observer.onSubscribe(set);
    
            final AtomicBoolean once = new AtomicBoolean();
    
            for (int i = 0; i < count; i++) {
                CompletableSource c = this.sources[i];
                if (set.isDisposed()) {
                    return;
                }
                if (c == null) {
                    NullPointerException npe = new NullPointerException("One of the this.sources is null");
                    if (once.compareAndSet(false, true)) {
                        set.dispose();
                        observer.onError(npe);
                    } else {
                        RxJavaPlugins.onError(npe);
                    }
                    return;
                }
    
                // no need to have separate subscribers because inner is stateless
                c.subscribe(new Amb(once, set, observer));
            }
    
            if (count == 0) {
                observer.onComplete();
            }
        }
}
