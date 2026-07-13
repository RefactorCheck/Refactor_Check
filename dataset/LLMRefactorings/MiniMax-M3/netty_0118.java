public class netty_0118 {

    @Setup
    public void setup(BurstCostExecutorsBenchmark bench) {
        final int work = bench.work;
        if (work > 0) {
            completeTask = new Runnable() {
                @Override
                public void run() {
                    Blackhole.consumeCPU(work);
                    markTaskCompleted();
                }
            };
        } else {
            completeTask = new Runnable() {
                @Override
                public void run() {
                    markTaskCompleted();
                }
            };
        }
    }

    private void markTaskCompleted() {
        DONE_UPDATER.lazySet(PerThreadState.this, completed + 1);
    }
}
