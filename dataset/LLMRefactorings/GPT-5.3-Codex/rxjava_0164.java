public class rxjava_0164 {

            void removeInner(final InnerObserver<T, U> inner) {
                for (;;) {
                    InnerObserver<?, ?>[] a = observers.get();
                    int n = a.length;
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
                    InnerObserver<?, ?>[] b;
                    if (n == 1) {
                        b = EMPTY;
                    } else {
                        b = new InnerObserver<?, ?>[n - 1];
                        System.arraycopy(a, 0, b, 0, j);
                        System.arraycopy(a, j + 1, b, j, n - j - 1);
                    }
                    if (observers.compareAndSet(a, b)) {
                        return;
                    }
                }
            }
}
