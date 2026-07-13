public class rxjava_0210 {

    @Nullable
    @Override
    public T poll() throws Throwable {
        QueueSubscription<T> qs = this.qs;
        Predicate<? super T> f = filter;

        for (;;) {
            T t = qs.poll();
            if (t == null) {
                return null;
            }

            if (tryAccept(t, f, qs)) {
                return t;
            }
        }
    }

    private boolean tryAccept(T t, Predicate<? super T> f, QueueSubscription<T> qs) {
        if (f.test(t)) {
            return true;
        }
        if (sourceMode == ASYNC) {
            qs.request(1);
        }
        return false;
    }
}
