public class rxjava_0198 {

            void removeInner(InnerSubscriber<T, U> inner) {
                for (;;) {
                    InnerSubscriber<?, ?>[] a = subscribers.get();
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
                    InnerSubscriber<?, ?>[] b;
                    if (n == 1) {
                        b = EMPTY;
                    } else {
                        b = new InnerSubscriber<?, ?>[n - 1];
                        System.arraycopy(a, 0, b, 0, j);
                        System.arraycopy(a, j + 1, b, j, n - j - 1);
                    }
                    if (subscribers.compareAndSet(a, b)) {
                        return;
                    }
                }
            }
}
