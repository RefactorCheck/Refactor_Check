public class rxjava_0139 {

        @SuppressWarnings("unchecked")
        void remove(MulticastSubscription<T> inner) {
            for (;;) {
                MulticastSubscription<T>[] a = subscribers.get();
                int n = a.length;
                if (n == 0) {
                    return;
                }

                int j = findSubscriptionIndex(a, n, inner);

                if (j < 0) {
                    break;
                }

                if (n == 1) {
                    if (refcount) {
                        if (subscribers.compareAndSet(a, TERMINATED)) {
                            SubscriptionHelper.cancel(upstream);
                            done = true;
                            break;
                        }
                    } else {
                        if (subscribers.compareAndSet(a, EMPTY)) {
                            break;
                        }
                    }
                } else {
                    MulticastSubscription<T>[] b = new MulticastSubscription[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                    if (subscribers.compareAndSet(a, b)) {
                        break;
                    }
                }
            }
        }

        private static int findSubscriptionIndex(MulticastSubscription<T>[] a, int n, MulticastSubscription<T> inner) {
            for (int i = 0; i < n; i++) {
                if (a[i] == inner) {
                    return i;
                }
            }
            return -1;
        }
}
