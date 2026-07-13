public class rxjava_0212 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }

        if (sourceMode != NONE) {
            downstream.onNext(null);
            return;
        }

        if (!tryAccept(t)) {
            return;
        }

        downstream.onNext(t);
    }

    private boolean tryAccept(T t) {
        try {
            onNext.accept(t);
            return true;
        } catch (Throwable e) {
            fail(e);
            return false;
        }
    }
}
