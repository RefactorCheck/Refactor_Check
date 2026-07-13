public class rxjava_0059 {

    @SuppressWarnings("unchecked")
    void remove(AsyncDisposable<T> ps) {
        for (;;) {
            AsyncDisposable<T>[] a = subscribers.get();
            int n = a.length;
            if (n == 0) {
                return;
            }

            int j = findIndex(a, ps, n);

            if (j < 0) {
                return;
            }

            AsyncDisposable<T>[] b;

            if (n == 1) {
                b = EMPTY;
            } else {
                b = new AsyncDisposable[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    private int findIndex(AsyncDisposable<T>[] a, AsyncDisposable<T> ps, int n) {
        for (int i = 0; i < n; i++) {
            if (a[i] == ps) {
                return i;
            }
        }
        return -1;
    }
}
