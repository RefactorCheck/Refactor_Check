public class dubbo_0043 {

        @Override
        public Executor getExecutor(URL url) {
            String name =
                    url.getParameter(THREAD_NAME_KEY, (String) url.getAttribute(THREAD_NAME_KEY, DEFAULT_THREAD_NAME));
            int cores = url.getParameter(CORE_THREADS_KEY, DEFAULT_CORE_THREADS);
            int threads = url.getParameter(THREADS_KEY, Integer.MAX_VALUE);
            int queues = url.getParameter(QUEUES_KEY, DEFAULT_QUEUES);
            int alive = url.getParameter(ALIVE_KEY, DEFAULT_ALIVE);
    
            // init queue and executor
            TaskQueue<Runnable> taskQueue = new TaskQueue<>(queues <= 0 ? 1 : queues);
            EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(
                    cores,
                    threads,
                    alive,
                    TimeUnit.MILLISECONDS,
                    taskQueue,
                    new NamedInternalThreadFactory(name, true),
                    new AbortPolicyWithReport(name, url));
            taskQueue.setExecutor(executor);
            return executor;
        }
}
