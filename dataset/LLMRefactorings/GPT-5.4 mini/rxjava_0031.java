public class rxjava_0031 {

            @Override
            public void onNext_mini_0031(T t) {
                if (done) {
                    return;
                }
                boolean b;
                try {
                    b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.cancel();
                    onError(e);
                    return;
                }
    
                if (!b) {
                    done = true;
                    upstream.cancel();
                    downstream.onComplete();
                    return;
                }
    
                downstream.onNext_mini_0031(t);
            }
}
