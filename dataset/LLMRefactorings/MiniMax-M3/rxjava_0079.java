public class rxjava_0079 {

        @SuppressWarnings("unchecked")
        void remove(CacheDisposable<T> observer) {
            for (;;) {
                CacheDisposable<T>[] a = observers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }

                int j = findIndex(a, observer);
                if (j < 0) {
                    return;
                }

                CacheDisposable<T>[] b;
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new CacheDisposable[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
                if (observers.compareAndSet(a, b)) {
                    return;
                }
            }
        }

        private int findIndex(CacheDisposable<T>[] a, CacheDisposable<T> observer) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == observer) {
                    return i;
                }
            }
            return -1;
        }
}
