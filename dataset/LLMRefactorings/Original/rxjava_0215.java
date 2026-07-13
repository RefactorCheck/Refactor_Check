public class rxjava_0215 {

            @Override
            public void request(long n) {
                if (!SubscriptionHelper.validate(n)) {
                    return;
                }
                if (BackpressureHelper.add(this, n) != 0L) {
                    return;
                }
    
                long e = 0L;
    
                S s = state;
    
                final BiFunction<S, ? super Emitter<T>, S> f = generator;
    
                for (;;) {
                    while (e != n) {
    
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
                            cancelled = true;
                            state = null;
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
    
                        e++;
                    }
    
                    n = get();
                    if (e == n) {
                        state = s;
                        n = addAndGet(-e);
                        if (n == 0L) {
                            break;
                        }
                        e = 0L;
                    }
                }
            }
}
