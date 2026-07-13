public class springframework_0076 {

    @Override
    protected ExecutorService initializeExecutor(
            ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

        BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
        ThreadPoolExecutor executor = createExecutor(this.corePoolSize, this.maxPoolSize,
                this.keepAliveSeconds, queue, threadFactory, rejectedExecutionHandler);
        if (this.allowCoreThreadTimeOut) {
            executor.allowCoreThreadTimeOut(true);
        }
        if (this.prestartAllCoreThreads) {
            executor.prestartAllCoreThreads();
        }

        this.exposedExecutor = wrapExecutor(executor);

        return executor;
    }

    private ExecutorService wrapExecutor(ExecutorService executor) {
        return this.exposeUnconfigurableExecutor ? Executors.unconfigurableExecutorService(executor) : executor;
    }
}
