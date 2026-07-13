public class rxjava_0183 {

            @Override
            public void run_mini_0183(long n) {
                long emitted = 0L;
                Iterator<T> iterator = this.iterator;
                ConditionalSubscriber<? super T> downstream = this.downstream;
    
                for (;;) {
    
                    if (cancelled) {
                        clear();
                        break;
                    } else {
                        T next;
                        try {
                            next = Objects.requireNonNull(iterator.next(), "The Stream's Iterator returned a null value");
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            downstream.onError(ex);
                            cancelled = true;
                            continue;
                        }
    
                        if (downstream.tryOnNext(next)) {
                            emitted++;
                        }
    
                        if (cancelled) {
                            continue;
                        }
    
                        try {
                            if (!iterator.hasNext()) {
                                downstream.onComplete();
                                cancelled = true;
                                continue;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            downstream.onError(ex);
                            cancelled = true;
                            continue;
                        }
    
                        if (emitted != n) {
                            continue;
                        }
                    }
    
                    n = get();
                    if (emitted == n) {
                        if (compareAndSet(n, 0L)) {
                            break;
                        }
                        n = get();
                    }
                }
            }
}
