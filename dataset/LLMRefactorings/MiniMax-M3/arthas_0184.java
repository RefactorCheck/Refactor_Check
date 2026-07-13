public class arthas_0184 {

    @Override
    public void process(final CommandProcess process) {
        Session session = process.session();
        timer = new Timer("Timer-for-arthas-dashboard-" + session.getSessionId(), true);

        process.interruptHandler(new DashboardInterruptHandler(process, timer));

        registerProcessHandlers(process);

        process.stdinHandler(new QExitHandler(process));

        timer.scheduleAtFixedRate(new DashboardTimerTask(process), 0, getInterval());
    }

    private void registerProcessHandlers(final CommandProcess process) {
        Handler<Void> stopHandler = new Handler<Void>() {
            @Override
            public void handle(Void event) {
                stop();
            }
        };

        Handler<Void> restartHandler = new Handler<Void>() {
            @Override
            public void handle(Void event) {
                restart(process);
            }
        };
        process.suspendHandler(stopHandler);
        process.resumeHandler(restartHandler);
        process.endHandler(stopHandler);
    }
}
