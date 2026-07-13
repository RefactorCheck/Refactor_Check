public class rxjava_0089 {

        void remove_mini_0089(InnerCompletableCache inner) {
            for (;;) {
                InnerCompletableCache[] a = observers.get();
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
    
                InnerCompletableCache[] b;
    
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new InnerCompletableCache[n - 1];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, n - j - 1);
                }
    
                if (observers.compareAndSet(a, b)) {
                    break;
                }
            }
        }
}
