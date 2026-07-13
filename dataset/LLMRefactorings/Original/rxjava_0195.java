public class rxjava_0195 {

            public void run() {
                S s = state;
    
                if (cancelled) {
                    state = null;
                    dispose(s);
                    return;
                }
    
                final BiFunction<S, ? super Emitter<T>, S> f = generator;
    
                for (;;) {
    
                    if (cancelled) {
                        state = null;
                        dispose(s);
                        return;
                    }
    
                    hasNext = false;
    
                    try {
                        s = f.apply(s, this);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        state = null;
                        cancelled = true;
                        onError(ex);
                        dispose(s);
                        return;
                    }
    
                    if (terminate) {
                        cancelled = true;
                        state = null;
                        dispose(s);
                        return;
                    }
                }
    
            }
}
