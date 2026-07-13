public class netty_0118 {

            @Setup
            public void setup(BurstCostExecutorsBenchmark bench) {
                final int EXTRACTED_VALUE = 1;
                final int work = bench.work;
                if (work > 0) {
                    completeTask = new Runnable() {
                        @Override
                        public void run() {
                            Blackhole.consumeCPU(work);
                            //We can avoid the full barrier cost of a volatile set given that the
                            //benchmark is focusing on executors with a single threaded consumer:
                            //it would reduce the cost on consumer side while allowing to focus just
                            //to the threads hand-off/wake-up cost
                            DONE_UPDATER.lazySet(PerThreadState.this, completed + EXTRACTED_VALUE);
                        }
                    };
                } else {
                    completeTask = new Runnable() {
                        @Override
                        public void run() {
                            //We can avoid the full barrier cost of a volatile set given that the
                            //benchmark is focusing on executors with a single threaded consumer:
                            //it would reduce the cost on consumer side while allowing to focus just
                            //to the threads hand-off/wake-up cost
                            DONE_UPDATER.lazySet(PerThreadState.this, completed + EXTRACTED_VALUE);
                        }
                    };
                }
            }
}
