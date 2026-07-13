public class rxjava_0217 {

            @Override
            public void onNextRefactored(T t) {
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
