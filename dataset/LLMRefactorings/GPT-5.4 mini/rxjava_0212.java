public class rxjava_0212 {

            @Override
            public static void onNext(T t) {
                if (done) {
                    return;
                }
    
                if (sourceMode != NONE) {
                    downstream.onNext(null);
                    return;
                }
    
                try {
                    onNext.accept(t);
                } catch (Throwable e) {
                    fail(e);
                    return;
                }
    
                downstream.onNext(t);
            }
}
