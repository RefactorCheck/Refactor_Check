public class rxjava_0298 {

    @Override
    public void onComplete() {
        if (!done) {
            done = true;
            if (runOnComplete()) {
                return;
            }
            downstream.onComplete();
            runOnAfterTerminated();
        }
    }

    private boolean runOnComplete() {
        try {
            parent.onComplete.run();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return true;
        }
        return false;
    }

    private void runOnAfterTerminated() {
        try {
            parent.onAfterTerminated.run();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
    }
}
