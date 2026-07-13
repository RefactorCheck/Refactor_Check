public class rxjava_0132 {

        @Override
        public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
            Objects.requireNonNull(run, "run is null");
            Objects.requireNonNull(unit, "unit is null");
            if (shutdown.get()) {
                return Disposable.disposed();
            }
    
            final BlockingDirectTask task = new BlockingDirectTask(run);
    
            if (delay == 0L) {
                enqueue(task);
                return task;
            }
    
            return scheduleDelayed(task, delay, unit);
        }
    
        private Disposable scheduleDelayed(BlockingDirectTask task, long delay, TimeUnit unit) {
            SequentialDisposable inner = new SequentialDisposable();
            final SequentialDisposable outer = new SequentialDisposable(inner);
    
            Disposable d = timedHelper.scheduleDirect(new RunInDirect(task, outer), delay, unit);
    
            if (d == Disposable.disposed()) {
                return d;
            }
    
            inner.replace(d);
    
            return outer;
        }
}
