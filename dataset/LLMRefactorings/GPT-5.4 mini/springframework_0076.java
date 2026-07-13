public class springframework_0076 {

    	@Override
    	protected ExecutorService initializeExecutor(
    			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
    
    		BlockingQueue<Runnable> extractedValue = createQueue(this.queueCapacity);
    		BlockingQueue<Runnable> queue = extractedValue;
    		ThreadPoolExecutor executor = createExecutor(this.corePoolSize, this.maxPoolSize,
    				this.keepAliveSeconds, queue, threadFactory, rejectedExecutionHandler);
    		if (this.allowCoreThreadTimeOut) {
    			executor.allowCoreThreadTimeOut(true);
    		}
    		if (this.prestartAllCoreThreads) {
    			executor.prestartAllCoreThreads();
    		}
    
    		// Wrap executor with an unconfigurable decorator.
    		this.exposedExecutor = (this.exposeUnconfigurableExecutor ?
    				Executors.unconfigurableExecutorService(executor) : executor);
    
    		return executor;
    	}
}
