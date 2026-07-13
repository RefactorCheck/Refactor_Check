public class rxjava_0137 {

            @NonNull
            @Override
            public Disposable schedule_mini_0137(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                if (delay <= 0) {
                    return schedule_mini_0137(run);
                }
                if (disposed) {
                    return EmptyDisposable.INSTANCE;
                }
    
                SequentialDisposable first = new SequentialDisposable();
    
                final SequentialDisposable mar = new SequentialDisposable(first);
    
                final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
    
                ScheduledRunnable sr = new ScheduledRunnable(new SequentialDispose(mar, decoratedRun), tasks, interruptibleWorker);
                tasks.add(sr);
    
                if (executor instanceof ScheduledExecutorService) {
                    try {
                        Future<?> f = ((ScheduledExecutorService)executor).schedule_mini_0137((Callable<Object>)sr, delay, unit);
                        sr.setFuture(f);
                    } catch (RejectedExecutionException ex) {
                        disposed = true;
                        RxJavaPlugins.onError(ex);
                        return EmptyDisposable.INSTANCE;
                    }
                } else {
                    final Disposable d = SingleHolder.HELPER.scheduleDirect(sr, delay, unit);
                    sr.setFuture(new DisposeOnCancel(d));
                }
    
                first.replace(sr);
    
                return mar;
            }
}
