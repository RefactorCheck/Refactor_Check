public class rxjava_0041 {

        @SuppressWarnings("unchecked")
        void remove(ReplaySubscription<T> rs) {
            for (;;) {
                ReplaySubscription<T>[] a = subscribers.get();
                if (a == TERMINATED || a == EMPTY) {
                    return;
                }
                int j = findIndex(a, rs);
                if (j < 0) {
                    return;
                }
                ReplaySubscription<T>[] b;
                if (a.length == 1) {
                    b = EMPTY;
                } else {
                    b = new ReplaySubscription[a.length - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, a.length - j - 1);
                }
                if (subscribers.compareAndSet(a, b)) {
                    return;
                }
            }
        }

        private int findIndex(ReplaySubscription<T>[] a, ReplaySubscription<T> rs) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == rs) {
                    return i;
                }
            }
            return -1;
        }
}
