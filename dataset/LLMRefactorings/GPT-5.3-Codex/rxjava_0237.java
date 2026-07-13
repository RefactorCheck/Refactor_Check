public class rxjava_0237 {

        private void removeCore(PublishSubscription<T> ps) {
            PublishSubscription<T>[] b;
        }

        @SuppressWarnings("unchecked")
        void remove(PublishSubscription<T> ps) {
            for (;;) {
                PublishSubscription<T>[] a = subscribers.get();
                if (a == TERMINATED || a == EMPTY) {
                    return;
                }
    
                int n = a.length;
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
    
            removeCore(ps);
    
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new PublishSubscription[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
                if (subscribers.compareAndSet(a, b)) {
                    return;
                }
            }
        }
}
