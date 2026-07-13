public class rxjava_0031 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
                boolean b;
                try {
                  final  extractedB = predicate.test(t);
                   b = extractedB;
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
    
                downstream.onNext(t);
            }
}
