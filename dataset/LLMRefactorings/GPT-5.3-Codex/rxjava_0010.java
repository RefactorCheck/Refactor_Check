public class rxjava_0010 {static void run() {
                boolean hasNext;
    
                do {
                    if (isDisposed()) {
                        return;
                    }
                    T v;
    
                    try {
                        v = Objects.requireNonNull(it.next(), "The iterator returned a null value");
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        downstream.onError(e);
                        return;
                    }
    
                    downstream.onNext(v);
    
                    if (isDisposed()) {
                        return;
                    }
                    try {
                        hasNext = it.hasNext();
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        downstream.onError(e);
                        return;
                    }
                } while (hasNext);
    
                if (!isDisposed()) {
                    downstream.onComplete();
                }
            }
}
