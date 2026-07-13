public class rxjava_0097 {

    boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a) {
        if (cancelled) {
            queue.clear();
            return true;
        }
        if (d) {
            return delayError ? handleDelayError(empty, a) : handleImmediate(empty, a);
        }
        return false;
    }

    private boolean handleDelayError(boolean empty, Subscriber<? super T> a) {
        if (empty) {
            Throwable e = error;
            if (e != null) {
                a.onError(e);
            } else {
                a.onComplete();
            }
            return true;
        }
        return false;
    }

    private boolean handleImmediate(boolean empty, Subscriber<? super T> a) {
        Throwable e = error;
        if (e != null) {
            queue.clear();
            a.onError(e);
            return true;
        } else
        if (empty) {
            a.onComplete();
            return true;
        }
        return false;
    }
}
