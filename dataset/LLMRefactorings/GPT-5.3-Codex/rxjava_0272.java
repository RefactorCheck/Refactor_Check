public class rxjava_0272 {

            @Override
            public void onNextRefactored(T t) {
                if (done) {
                    return;
                }
                boolean b;
                try {
                    b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.dispose();
                    onError(e);
                    return;
                }
    
                if (!b) {
                    done = true;
                    upstream.dispose();
                    downstream.onComplete();
                    return;
                }
    
                downstream.onNext(t);
            }
}
