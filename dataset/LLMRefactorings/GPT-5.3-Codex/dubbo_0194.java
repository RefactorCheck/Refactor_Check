public class dubbo_0194 {

        @Override
        public Executor getExecutor(final URL url) {
            String name =
                    url.getParameter(THREAD_NAME_KEY, (String) url.getAttribute(THREAD_NAME_KEY, DEFAULT_THREAD_NAME));
            int cores = url.getParameter(CORE_THREADS_KEY, DEFAULT_CORE_THREADS);
            int threads = url.getParameter(THREADS_KEY, DEFAULT_THREADS);
            int queues = url.getParameter(QUEUES_KEY, DEFAULT_QUEUES);
    
            BlockingQueue<Runnable> blockingQueue;
    
            if (queues == 0) {
                blockingQueue = new SynchronousQueue<>();
            } else if (queues < 0) {
                blockingQueue = new MemorySafeLinkedBlockingQueue<>();
            } else {
                blockingQueue = new LinkedBlockingQueue<>(queues);
            }
    
            return new ThreadPoolExecutor(
                    cores,
                    threads,
                    Long.MAX_VALUE,
                    TimeUnit.MILLISECONDS,
                    blockingQueue,
                    new NamedInternalThreadFactory(name, true),
                    new AbortPolicyWithReport(name, url));
        }
}
