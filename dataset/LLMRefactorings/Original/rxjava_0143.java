public class rxjava_0143 {

            @NonNull
            public Disposable schedulePeriodically(@NonNull Runnable run, final long initialDelay, final long period, @NonNull final TimeUnit unit) {
                final SequentialDisposable first = new SequentialDisposable();
    
                final SequentialDisposable sd = new SequentialDisposable(first);
    
                final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
    
                final long periodInNanoseconds = unit.toNanos(period);
                final long firstNowNanoseconds = now(TimeUnit.NANOSECONDS);
                final long firstStartInNanoseconds = firstNowNanoseconds + unit.toNanos(initialDelay);
    
                Disposable d = schedule(new PeriodicTask(firstStartInNanoseconds, decoratedRun, firstNowNanoseconds, sd,
                        periodInNanoseconds), initialDelay, unit);
    
                if (d == EmptyDisposable.INSTANCE) {
                    return d;
                }
                first.replace(d);
    
                return sd;
            }
}
