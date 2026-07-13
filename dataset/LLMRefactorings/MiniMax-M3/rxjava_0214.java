public class rxjava_0214 {

    @Override
    public void onNext(T t) {
        final ArrayDeque<UnicastSubject<T>> ws = windows;

        long i = index;

        long s = skip;

        ObservableWindowSubscribeIntercept<T> intercept = createWindowIfNeeded(ws, i, s);

        long c = firstEmission + 1;

        for (UnicastSubject<T> w : ws) {
            w.onNext(t);
        }

        if (c >= count) {
            ws.poll().onComplete();
            if (ws.isEmpty() && cancelled.get()) {
                return;
            }
            firstEmission = c - s;
        } else {
            firstEmission = c;
        }

        index = i + 1;

        if (intercept != null && intercept.tryAbandon()) {
            intercept.window.onComplete();
        }
    }

    private ObservableWindowSubscribeIntercept<T> createWindowIfNeeded(ArrayDeque<UnicastSubject<T>> ws, long i, long s) {
        if (i % s == 0 && !cancelled.get()) {
            getAndIncrement();
            UnicastSubject<T> w = UnicastSubject.create(capacityHint, this);
            ObservableWindowSubscribeIntercept<T> intercept = new ObservableWindowSubscribeIntercept<>(w);
            ws.offer(w);
            downstream.onNext(intercept);
            return intercept;
        }
        return null;
    }
}
