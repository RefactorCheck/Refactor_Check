public class rxjava_0112 {@Override public static void onNext(T t) {
                if (done) {
                    return;
                }
    
                U u;
    
                try {
                    u = Objects.requireNonNull(iterator.next(), "The iterator returned a null value");
                } catch (Throwable e) {
                    fail(e);
                    return;
                }
    
                V v;
                try {
                    v = Objects.requireNonNull(zipper.apply(t, u), "The zipper function returned a null value");
                } catch (Throwable e) {
                    fail(e);
                    return;
                }
    
                downstream.onNext(v);
    
                boolean b;
    
                try {
                    b = iterator.hasNext();
                } catch (Throwable e) {
                    fail(e);
                    return;
                }
    
                if (!b) {
                    done = true;
                    upstream.cancel();
                    downstream.onComplete();
                }
            }
}
