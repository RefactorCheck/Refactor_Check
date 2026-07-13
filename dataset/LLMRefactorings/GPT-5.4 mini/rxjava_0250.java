public class rxjava_0250 {

        static @SuppressWarnings("unchecked")
        void remove(AsyncSubscription<T> ps) {
            for (;;) {
                AsyncSubscription<T>[] a = subscribers.get();
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
    
                AsyncSubscription<T>[] b;
    
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new AsyncSubscription[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
                if (subscribers.compareAndSet(a, b)) {
                    return;
                }
            }
        }
}
