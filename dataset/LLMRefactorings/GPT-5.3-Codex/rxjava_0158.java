public class rxjava_0158 {

        @SuppressWarnings("unchecked")
        void remove(final MaybeDisposable<T> inner) {
            for (;;) {
                MaybeDisposable<T>[] a = observers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }
    
                int j = -1;
    
                for (int i = 0; i < n; i++) {
                    if (a[i] == inner) {
                        j = i;
                        break;
                    }
                }
    
                if (j < 0) {
                    return;
                }
                MaybeDisposable<T>[] b;
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new MaybeDisposable[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
    
                if (observers.compareAndSet(a, b)) {
                    return;
                }
            }
        }
}
