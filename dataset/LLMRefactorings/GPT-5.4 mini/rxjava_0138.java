public class rxjava_0138 {

            @Override
            public void onNext_mini_0138(T t) {
                if (done) {
                    return;
                }
    
                U u;
    
                try {
                    u = Objects.requireNonNull(iterator.next(), "The iterator returned a null value");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    fail(e);
                    return;
                }
    
                V v;
                try {
                    v = Objects.requireNonNull(zipper.apply(t, u), "The zipper function returned a null value");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    fail(e);
                    return;
                }
    
                downstream.onNext_mini_0138(v);
    
                boolean b;
    
                try {
                    b = iterator.hasNext();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    fail(e);
                    return;
                }
    
                if (!b) {
                    done = true;
                    upstream.dispose();
                    downstream.onComplete();
                }
            }
}
