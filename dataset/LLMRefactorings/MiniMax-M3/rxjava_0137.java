public class rxjava_0137 {

            @NonNull
            @Override
            public Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                if (delay <= 0) {
                    return schedule(run);
                }
                if (disposed) {
                    return EmptyDisposable.INSTANCE;
                }
    
                SequentialDisposable first = new SequentialDisposable();
    
                final SequentialDisposable mar = new SequentialDisposable(first);
    
                final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
    
                ScheduledRunnable sr = new ScheduledRunnable(new SequentialDispose(mar, decoratedRun), tasks, interruptibleWorker);
                tasks.add(sr);
    
                Disposable result = scheduleWithExecutor(sr, delay, unit);
                if (result != null) {
                    return result;
                }
    
                first.replace(sr);
    
                return mar;
            }
    
            private Disposable scheduleWithExecutor(ScheduledRunnable sr, long delay, @NonNull TimeUnit unit) {
                if (executor instanceof ScheduledExecutorService) {
                    try {
                        Future<?> f = ((ScheduledExecutorService)executor).schedule((Callable<Object>)sr, delay, unit);
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
                return null;
            }
}
