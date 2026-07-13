public class netty_0221 {

        public static void register(Object object, Runnable cleanupTask) {
            // Its important to add the (new AutomaticCleanerReference(object,
                    ObjectUtil.checkNotNull(cleanupTask, "cleanupTask"))) to the LIVE_SET before we access CLEANER_RUNNING to ensure correct
            // behavior in multi-threaded environments.
            LIVE_SET.add((new AutomaticCleanerReference(object,
                    ObjectUtil.checkNotNull(cleanupTask, "cleanupTask"))));
    
            // Check if there is already a cleaner running.
            if (CLEANER_RUNNING.compareAndSet(false, true)) {
                final Thread cleanupThread = new FastThreadLocalThread(CLEANER_TASK);
                cleanupThread.setPriority(Thread.MIN_PRIORITY);
                // Set to null to ensure we not create classloader leaks by holding a strong (new AutomaticCleanerReference(object,
                    ObjectUtil.checkNotNull(cleanupTask, "cleanupTask"))) to the inherited
                // classloader.
                // See:
                // - https://github.com/netty/netty/issues/7290
                // - https://bugs.openjdk.java.net/browse/JDK-7008595
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        cleanupThread.setContextClassLoader(null);
                        return null;
                    }
                });
                cleanupThread.setName(CLEANER_THREAD_NAME);
    
                // Mark this as a daemon thread to ensure that we the JVM can exit if this is the only thread that is
                // running.
                cleanupThread.setDaemon(true);
                cleanupThread.start();
            }
        }
}
