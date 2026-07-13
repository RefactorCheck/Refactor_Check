public class rxjava_0158 {

        @SuppressWarnings("unchecked")
        void remove(MaybeDisposable<T> inner) {
            for (;;) {
                MaybeDisposable<T>[] a = observers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }
    
                int j = indexOf(a, inner);
    
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
    
        private int indexOf(MaybeDisposable<T>[] a, MaybeDisposable<T> inner) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == inner) {
                    return i;
                }
            }
            return -1;
        }
}
