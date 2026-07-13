public class rxjava_0099 {

            @Override
            public void run() {
                if (!worker.disposed) {
                    long t = worker.now(TimeUnit.MILLISECONDS);
                    if (execTime > t) {
                        long delay = execTime - t;
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            RxJavaPlugins.onError(e);
                            return;
                        }
                    }
    
                    if (!worker.disposed) {
                        run.run();
                    }
                }
            }
}
