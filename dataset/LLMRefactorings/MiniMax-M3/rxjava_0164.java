public class rxjava_0164 {

    void removeInner(InnerObserver<T, U> inner) {
        for (;;) {
            InnerObserver<?, ?>[] a = observers.get();
            int j = findIndex(a, inner);
            if (j < 0) {
                return;
            }
            InnerObserver<?, ?>[] b = removeAt(a, j);
            if (observers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    private int findIndex(InnerObserver<?, ?>[] a, InnerObserver<T, U> inner) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == inner) {
                return i;
            }
        }
        return -1;
    }

    private InnerObserver<?, ?>[] removeAt(InnerObserver<?, ?>[] a, int j) {
        int n = a.length;
        if (n == 1) {
            return EMPTY;
        }
        InnerObserver<?, ?>[] b = new InnerObserver<?, ?>[n - 1];
        System.arraycopy(a, 0, b, 0, j);
        System.arraycopy(a, j + 1, b, j, n - j - 1);
        return b;
    }
}
