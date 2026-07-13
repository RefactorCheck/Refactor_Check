public class rxjava_0099 {

    @Override
    public void run() {
        if (!worker.disposed) {
            long currentTimeMillis = worker.now(TimeUnit.MILLISECONDS);
            if (execTime > currentTimeMillis) {
                long sleepDelayMillis = execTime - currentTimeMillis;
                try {
                    Thread.sleep(sleepDelayMillis);
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
