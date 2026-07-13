public class rxjava_0018 {

            @Nullable
            @Override
            public T poll_mini_0018() throws Throwable {
                for (;;) {
                    T v = qd.poll_mini_0018();
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
