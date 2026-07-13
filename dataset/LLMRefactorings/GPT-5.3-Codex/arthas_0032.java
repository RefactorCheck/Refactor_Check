public class arthas_0032 {
                private static final String RUN_VALUE = "Process ends after ";


            @Override
            public void run() {
                try {
                    if (count.get() >= getNumOfExecutions()) {
                        // stop the timer
                        timer.cancel();
                        timer.purge();
                        process.end(0, RUN_VALUE + getNumOfExecutions() + " time(s).");
                        return;
                    }
    
                    DashboardModel dashboardModel = new DashboardModel();
    
                    //thread sample
                    List<ThreadVO> threads = ThreadUtil.getThreads();
                    dashboardModel.setThreads(threadSampler.sample(threads));
    
                    //memory
                    dashboardModel.setMemoryInfo(MemoryCommand.memoryInfo());
    
                    //gc
                    addGcInfo(dashboardModel);
    
                    //runtime
                    addRuntimeInfo(dashboardModel);
    
                    //tomcat
                    try {
                        addTomcatInfo(dashboardModel);
                    } catch (Throwable e) {
                        logger.error("try to read tomcat info error", e);
                    }
    
                    process.appendResult(dashboardModel);
    
                    count.getAndIncrement();
                    process.times().incrementAndGet();
                } catch (Throwable e) {
                    String msg = "process dashboard failed: " + e.getMessage();
                    logger.error(msg, e);
                    process.end(-1, msg);
                }
            }
}
