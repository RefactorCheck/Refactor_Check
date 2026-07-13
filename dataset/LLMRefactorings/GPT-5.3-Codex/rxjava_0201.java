public class rxjava_0201 {

        public static <T> void subscribeStreamRefactored(Subscriber<? super T> s, Stream<T> stream) {
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
    
            if (s instanceof ConditionalSubscriber) {
                s.onSubscribe(new StreamConditionalSubscription<>((ConditionalSubscriber<? super T>)s, iterator, stream));
            } else {
                s.onSubscribe(new StreamSubscription<>(s, iterator, stream));
            }
        }
}
