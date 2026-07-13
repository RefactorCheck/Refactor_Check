public class netty_0265 {

        public static void runWithFastThreadLocalRenamed(Runnable runnable) {
            Thread current = currentThread();
            if (current instanceof FastThreadLocalThread) {
                throw new IllegalStateException("Caller is a real FastThreadLocalThread");
            }
            long id = current.getId();
            fallbackThreads.updateAndGet(set -> {
                if (set.contains(id)) {
                    throw new IllegalStateException("Reentrant call to run()");
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
