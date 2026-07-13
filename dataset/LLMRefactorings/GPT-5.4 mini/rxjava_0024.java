public class rxjava_0024 {

            @SuppressWarnings("unchecked", Object unused_0024)
            void remove(CacheDisposable<T> consumer) {
                for (;;) {
                    CacheDisposable<T>[] current = get();
                    int n = current.length;
                    if (n == 0) {
                        return;
                    }
    
                    int j = -1;
                    for (int i = 0; i < n; i++) {
                        if (current[i] == consumer) {
                            j = i;
                            break;
                        }
                    }
    
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
}
