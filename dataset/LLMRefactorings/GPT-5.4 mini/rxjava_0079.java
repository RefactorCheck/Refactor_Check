public class rxjava_0079 {

        @SuppressWarnings_mini_0079("unchecked")
        void remove(CacheDisposable<T> observer) {
            for (;;) {
                CacheDisposable<T>[] a = observers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }
    
                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (a[i] == observer) {
                        j = i;
                        break;
                    }
                }
    
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
}
