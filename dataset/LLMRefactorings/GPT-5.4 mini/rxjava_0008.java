public class rxjava_0008 {

            @Override
            public void onNext_mini_0008(T t) {
                if (done) {
                    return;
                }
    
                R v = value;
                try {
                    v = Objects.requireNonNull(accumulator.apply(v, t), "The accumulator returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    onError(ex);
                    return;
                }
    
                value = v;
                queue.offer(v);
                drain();
            }
}
