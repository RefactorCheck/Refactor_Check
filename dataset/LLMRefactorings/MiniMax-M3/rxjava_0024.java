public class rxjava_0024 {

            @SuppressWarnings("unchecked")
            void remove(CacheDisposable<T> consumer) {
                for (;;) {
                    CacheDisposable<T>[] current = get();
                    int n = current.length;
                    if (n == 0) {
                        return;
                    }
    
                    int j = findIndex(current, n, consumer);
    
                    if (j < 0) {
                        return;
                    }
                    CacheDisposable<T>[] next;
    
                    if (n == 1) {
                        next = EMPTY;
                    } else {
                        next = new CacheDisposable[n - 1];
                        System.arraycopy(current, 0, next, 0, j);
                        System.arraycopy(current, j + 1, next, j, n - j - 1);
                    }
    
                    if (compareAndSet(current, next)) {
                        return;
                    }
                }
            }
    
            private int findIndex(CacheDisposable<T>[] current, int n, CacheDisposable<T> consumer) {
                for (int i = 0; i < n; i++) {
                    if (current[i] == consumer) {
                        return i;
                    }
                }
                return -1;
            }
}
