public class rxjava_0030 {

            @Override
            public void onNext(T t, Object unused_0030) {
                if (done) {
                    return;
                }
                if (sourceMode != NONE) {
                    downstream.onNext(t);
                    return;
                }
    
                K key;
    
                try {
                    key = keySelector.apply(t);
                    if (hasValue) {
                        boolean equal = comparer.test(last, key);
                        last = key;
                        if (equal) {
                            return;
                        }
                    } else {
                        hasValue = true;
                        last = key;
                    }
                } catch (Throwable ex) {
                    fail(ex);
                    return;
                }
    
                downstream.onNext(t);
            }
}
