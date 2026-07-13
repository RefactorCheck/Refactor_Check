public class rxjava_0270 {

    @NonNull
    @Override
    public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
        final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (period <= 0L) {
            return scheduleInstantTask(decoratedRun, initialDelay, unit);
        }
        return scheduleFixedRateTask(decoratedRun, initialDelay, period, unit);
    }

    private Disposable scheduleInstantTask(Runnable decoratedRun, long initialDelay, TimeUnit unit) {
        ScheduledExecutorService exec = executor.get();
        InstantPeriodicTask periodicWrapper = new InstantPeriodicTask(decoratedRun, exec);
        Future<?> f;
        try {
            if (initialDelay <= 0L) {
                f = exec.submit(periodicWrapper);
            } else {
                f = exec.schedule(periodicWrapper, initialDelay, unit);
            }
            periodicWrapper.setFirst(f);
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
        return periodicWrapper;
    }

    private Disposable scheduleFixedRateTask(Runnable decoratedRun, long initialDelay, long period, TimeUnit unit) {
        ScheduledDirectPeriodicTask task = new ScheduledDirectPeriodicTask(decoratedRun, true);
        try {
            Future<?> f = executor.get().scheduleAtFixedRate(task, initialDelay, period, unit);
            task.setFuture(f);
            return task;
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }
}
