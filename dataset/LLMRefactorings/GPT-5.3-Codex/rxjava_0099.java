public class rxjava_0099 {

            @Override
            public void run() {
                if (!worker.disposed) {
                    if (execTime > worker.now(TimeUnit.MILLISECONDS)) {
                        long delay = execTime - worker.now(TimeUnit.MILLISECONDS);
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
