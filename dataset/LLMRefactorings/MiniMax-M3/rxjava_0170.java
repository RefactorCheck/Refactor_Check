public class rxjava_0170 {

    private static final long DEFAULT_TIMEOUT_MILLIS = 5000;
    private static final long SLEEP_INTERVAL_MILLIS = 10;

    @SuppressWarnings("unchecked")
    @NonNull
    public final U awaitCount(int atLeast) {
        long start = System.currentTimeMillis();
        for (;;) {
            if (System.currentTimeMillis() - start >= DEFAULT_TIMEOUT_MILLIS) {
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
                Thread.sleep(SLEEP_INTERVAL_MILLIS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        return (U)this;
    }
}
