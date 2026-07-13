public class rxjava_0191 {

        public static <T> void subscribe(Subscriber<? super T> s, Iterator<? extends T> it) {
            boolean hasNext;
            try {
                hasNext = it.hasNext();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptySubscription.error(e, s);
                return;
            }
    
            if (!hasNext) {
                EmptySubscription.complete(s);
                return;
            }
    
            if (s instanceof ConditionalSubscriber) {
                s.onSubscribe(new IteratorConditionalSubscription<T>(
                        (ConditionalSubscriber<? super T>)s, it));
            } else {
                s.onSubscribe(new IteratorSubscription<T>(s, it));
            }
        }
}
