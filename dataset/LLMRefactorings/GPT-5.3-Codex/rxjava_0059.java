public class rxjava_0059 {

        @SuppressWarnings("unchecked")
        void remove(AsyncDisposable<T> ps) {
            for (;;) {
                final AsyncDisposable<T>[] extractedA = subscribers.get();
                AsyncDisposable<T>[] a = extractedA;
                int n = a.length;
                if (n == 0) {
                    return;
                }
    
                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (a[i] == ps) {
                        j = i;
                        break;
                    }
                }
    
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
}
