public class rxjava_0045 {

            void fastPath(Subscriber<? super R> a, Iterator<? extends R> iterator) {
                for (;;) {
                    if (cancelled) {
                        return;
                    }
    
                    R v;
    
                    try {
                        v = iterator.next();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        a.onError(ex);
                        return;
                    }
    
                    a.onNext(v);
    
                    if (cancelled) {
                        return;
                    }
    
                    boolean b;
    
                    try {
                        b = iterator.hasNext();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        a.onError(ex);
                        return;
                    }
    
                    if (!b) {
                        a.onComplete();
                        return;
                    }
                }
            }
}
