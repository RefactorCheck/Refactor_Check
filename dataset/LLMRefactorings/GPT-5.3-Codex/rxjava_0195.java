public class rxjava_0195 {

            public void run() {
    
                if (cancelled) {
                    state = null;
                    dispose(state);
                    return;
                }
    
                final BiFunction<S, ? super Emitter<T>, S> f = generator;
    
                for (;;) {
    
                    if (cancelled) {
                        state = null;
                        dispose(state);
                        return;
                    }
    
                    hasNext = false;
    
                    try {
                        state = f.apply(state, this);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        state = null;
                        cancelled = true;
                        onError(ex);
                        dispose(state);
                        return;
                    }
    
                    if (terminate) {
                        cancelled = true;
                        state = null;
                        dispose(state);
                        return;
                    }
                }
    
            }
}
