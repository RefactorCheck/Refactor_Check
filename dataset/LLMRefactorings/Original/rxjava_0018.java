public class rxjava_0018 {

            @Nullable
            @Override
            public T poll() throws Throwable {
                for (;;) {
                    T v = qd.poll();
                    if (v == null) {
                        return null;
                    }
                    K key = keySelector.apply(v);
                    if (!hasValue) {
                        hasValue = true;
                        last = key;
                        return v;
                    }
    
                    if (!comparer.test(last, key)) {
                        last = key;
                        return v;
                    }
                    last = key;
                }
            }
}
