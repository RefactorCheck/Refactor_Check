public class rxjava_0142 {

    @Override
    public void onComplete() {
        if (done) {
            return;
        }

        done = true;

        if (upstream == null) {
            onCompleteNoSubscription();
            return;
        }

        completeDownstream();
    }

    private void completeDownstream() {
        try {
            downstream.onComplete();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
        }
    }
}
