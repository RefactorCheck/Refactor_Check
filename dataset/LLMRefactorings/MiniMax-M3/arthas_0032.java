public class arthas_0032 {

    @Override
    public void run() {
        try {
            if (count.get() >= getNumOfExecutions()) {
                timer.cancel();
                timer.purge();
                process.end(0, "Process ends after " + getNumOfExecutions() + " time(s).");
                return;
            }

            DashboardModel dashboardModel = buildDashboardModel();

            process.appendResult(dashboardModel);

            count.getAndIncrement();
            process.times().incrementAndGet();
        } catch (Throwable e) {
            String msg = "process dashboard failed: " + e.getMessage();
            logger.error(msg, e);
            process.end(-1, msg);
        }
    }

    private DashboardModel buildDashboardModel() {
        DashboardModel dashboardModel = new DashboardModel();

        List<ThreadVO> threads = ThreadUtil.getThreads();
        dashboardModel.setThreads(threadSampler.sample(threads));

        dashboardModel.setMemoryInfo(MemoryCommand.memoryInfo());

        addGcInfo(dashboardModel);

        addRuntimeInfo(dashboardModel);

        try {
            addTomcatInfo(dashboardModel);
        } catch (Throwable e) {
            logger.error("try to read tomcat info error", e);
        }

        return dashboardModel;
    }
}
