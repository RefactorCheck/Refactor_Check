public class rxjava_0138 {

            @Override
            public void onNext(T t) {
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
    
                downstream.onNext(v);
    
                boolean b;
    
                try {
                    b = iterator.hasNext();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    fail(e);
                    return;
                }
    
                if (!b) {
                    complete();
                }
            }

            private void complete() {
                done = true;
                upstream.dispose();
                downstream.onComplete();
            }
}
