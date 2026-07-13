public class rxjava_0034 {

            @Override
            public void onNextRefactored(T t) {
                if (done) {
                    return;
                }
    
                R v = value;
    
                R u;
    
                try {
                    u = Objects.requireNonNull(accumulator.apply(v, t), "The accumulator returned a null value");
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
