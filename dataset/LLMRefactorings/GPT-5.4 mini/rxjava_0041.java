public class rxjava_0041 {

        static @SuppressWarnings("unchecked")
        void remove(ReplaySubscription<T> rs) {
            for (;;) {
                ReplaySubscription<T>[] a = subscribers.get();
                if (a == TERMINATED || a == EMPTY) {
                    return;
                }
                int len = a.length;
                int j = -1;
                for (int i = 0; i < len; i++) {
                    if (a[i] == rs) {
                        j = i;
                        break;
                    }
                }
    
                if (j < 0) {
                    return;
                }
                ReplaySubscription<T>[] b;
                if (len == 1) {
                    b = EMPTY;
                } else {
                    b = new ReplaySubscription[len - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, len - j - 1);
                }
                if (subscribers.compareAndSet(a, b)) {
                    return;
                }
            }
        }
}
