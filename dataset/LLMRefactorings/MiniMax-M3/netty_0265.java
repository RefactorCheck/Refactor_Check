public class netty_0265 {

    private static final String FAST_THREAD_LOCAL_THREAD_MESSAGE = "Caller is a real FastThreadLocalThread";
    private static final String REENTRANT_CALL_MESSAGE = "Reentrant call to run()";

    public static void runWithFastThreadLocal(Runnable runnable) {
        Thread current = currentThread();
        if (current instanceof FastThreadLocalThread) {
            throw new IllegalStateException(FAST_THREAD_LOCAL_THREAD_MESSAGE);
        }
        long id = current.getId();
        fallbackThreads.updateAndGet(set -> {
            if (set.contains(id)) {
                throw new IllegalStateException(REENTRANT_CALL_MESSAGE);
            }
            return set.add(id);
        });

        try {
            runnable.run();
        } finally {
            fallbackThreads.getAndUpdate(set -> set.remove(id));
            FastThreadLocal.removeAll();
        }
    }
}
