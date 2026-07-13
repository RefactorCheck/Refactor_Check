public class rxjava_0250 {

        @SuppressWarnings("unchecked")
        void remove(AsyncSubscription<T> ps) {
            for (;;) {
                AsyncSubscription<T>[] a = subscribers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }
    
                int j = indexOf(a, ps, n);
                if (j < 0) {
                    return;
                }
    
                AsyncSubscription<T>[] b = removeAt(a, j, n);
                if (subscribers.compareAndSet(a, b)) {
                    return;
                }
            }
        }
    
        private int indexOf(AsyncSubscription<T>[] a, AsyncSubscription<T> ps, int n) {
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    return i;
                }
            }
            return -1;
        }
    
        @SuppressWarnings("unchecked")
        private AsyncSubscription<T>[] removeAt(AsyncSubscription<T>[] a, int j, int n) {
            if (n == 1) {
                return EMPTY;
            }
            AsyncSubscription<T>[] b = new AsyncSubscription[n - 1];
            System.arraycopy(a, 0, b, 0, j);
            System.arraycopy(a, j + 1, b, j, n - j - 1);
            return b;
        }
}
