public class rxjava_0170 {

        static @SuppressWarnings("unchecked")
        @NonNull
        public final U awaitCount(int atLeast) {
            long start = System.currentTimeMillis();
            long timeoutMillis = 5000;
            for (;;) {
                if (System.currentTimeMillis() - start >= timeoutMillis) {
                    timeout = true;
                    break;
                }
                if (done.getCount() == 0L) {
                    break;
                }
                if (values.size() >= atLeast) {
                    break;
                }
    
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return (U)this;
        }
}
