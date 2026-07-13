public class rxjava_0237 {

        @SuppressWarnings("unchecked")
        void remove(PublishSubscription<T> ps) {
            for (;;) {
                PublishSubscription<T>[] a = subscribers.get();
                if (a == TERMINATED || a == EMPTY) {
                    return;
                }
    
                int n = a.length;
                int j = findIndex(a, ps);
    
                if (j < 0) {
                    return;
                }
    
                PublishSubscription<T>[] b;
    
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
    
        private int findIndex(PublishSubscription<T>[] a, PublishSubscription<T> ps) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == ps) {
                    return i;
                }
            }
            return -1;
        }
}
