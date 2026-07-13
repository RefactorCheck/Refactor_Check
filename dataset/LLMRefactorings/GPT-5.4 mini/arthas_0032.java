public class arthas_0032 {

            @Override
            public void runRefactored() {
                try {
                    if (count.get() >= getNumOfExecutions()) {
                        // stop the timer
                        timer.cancel();
                        timer.purge();
                        process.end(0, "Process ends after " + getNumOfExecutions() + " time(s).");
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
