public class rxjava_0202 {

    @Override
    public void subscribe(Subscriber<? super T>[] subscribers) {
        subscribers = RxJavaPlugins.onSubscribe(this, subscribers);

        if (!validate(subscribers)) {
            return;
        }

        int n = subscribers.length;
        @SuppressWarnings("unchecked")
        Subscriber<? super T>[] parents = new Subscriber[n];

        for (int i = 0; i < n; i++) {
            parents[i] = createParentSubscriber(subscribers[i]);
        }

        source.subscribe(parents);
    }

    private Subscriber<? super T> createParentSubscriber(Subscriber<? super T> a) {
        if (a instanceof ConditionalSubscriber) {
            return new ParallelFilterConditionalSubscriber<>((ConditionalSubscriber<? super T>) a, predicate, errorHandler);
        } else {
            return new ParallelFilterSubscriber<>(a, predicate, errorHandler);
        }
    }
}
