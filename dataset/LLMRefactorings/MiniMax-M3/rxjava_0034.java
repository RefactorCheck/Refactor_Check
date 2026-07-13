public class rxjava_0034 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                R u;
    
                try {
                    u = Objects.requireNonNull(accumulator.apply(value, t), "The accumulator returned a null value");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.dispose();
                    onError(e);
                    return;
                }
    
                value = u;
    
                downstream.onNext(u);
            }
}
