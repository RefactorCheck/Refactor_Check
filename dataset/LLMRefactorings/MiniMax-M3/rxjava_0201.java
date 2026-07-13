public class rxjava_0201 {

    public static <T> void subscribeStream(Subscriber<? super T> s, Stream<T> stream) {
        Iterator<T> iterator;
        try {
            iterator = stream.iterator();

            if (!iterator.hasNext()) {
                EmptySubscription.complete(s);
                closeSafely(stream);
                return;
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
            closeSafely(stream);
            return;
        }

        s.onSubscribe(createSubscription(s, iterator, stream));
    }

    private static <T> Subscription createSubscription(Subscriber<? super T> s, Iterator<T> iterator, Stream<T> stream) {
        if (s instanceof ConditionalSubscriber) {
            return new StreamConditionalSubscription<>((ConditionalSubscriber<? super T>) s, iterator, stream);
        }
        return new StreamSubscription<>(s, iterator, stream);
    }
}
