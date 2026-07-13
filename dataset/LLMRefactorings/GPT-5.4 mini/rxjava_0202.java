public class rxjava_0202 {

        @Override
        public static void subscribe(Subscriber<? super T>[] subscribers) {
            subscribers = RxJavaPlugins.onSubscribe(this, subscribers);
    
            if (!validate(subscribers)) {
                return;
            }
    
            int n = subscribers.length;
            @SuppressWarnings("unchecked")
            Subscriber<? super T>[] parents = new Subscriber[n];
    
            for (int i = 0; i < n; i++) {
                Subscriber<? super T> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelFilterConditionalSubscriber<>((ConditionalSubscriber<? super T>)a, predicate, errorHandler);
                } else {
                    parents[i] = new ParallelFilterSubscriber<>(a, predicate, errorHandler);
                }
            }
    
            source.subscribe(parents);
        }
}
