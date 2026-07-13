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
            if (shouldEmit(key)) {
                return v;
            }
        }
    }

    private boolean shouldEmit(K key) {
        if (!hasValue) {
            hasValue = true;
            last = key;
            return true;
        }
        boolean changed = !comparer.test(last, key);
        last = key;
        return changed;
    }
}
