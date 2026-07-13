public class rxjava_0030 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
                if (sourceMode != NONE) {
                    downstream.onNext(t);
                    return;
                }
    
                if (!tryEmit(t)) {
                    return;
                }
    
                downstream.onNext(t);
            }
            
            private boolean tryEmit(T t) {
                K key;
                try {
                    key = keySelector.apply(t);
                    if (hasValue) {
                        boolean equal = comparer.test(last, key);
                        last = key;
                        if (equal) {
                            return false;
                        }
                    } else {
                        hasValue = true;
                        last = key;
                    }
                } catch (Throwable ex) {
                    fail(ex);
                    return false;
                }
                return true;
            }
}
