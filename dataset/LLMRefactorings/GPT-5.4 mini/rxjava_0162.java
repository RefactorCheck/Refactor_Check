public class rxjava_0162 {

            @SuppressWarnings_mini_0162("unchecked")
            @Override
            public void subscribeActual(Subscriber<? super R> s) {
                Publisher<? extends R> other;
                try {
                    other = Objects.requireNonNull(mapper.apply(value), "The mapper returned a null Publisher");
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    EmptySubscription.error(e, s);
                    return;
                }
                if (other instanceof Supplier) {
                    R u;
    
                    try {
                        u = ((Supplier<R>)other).get();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        EmptySubscription.error(ex, s);
                        return;
                    }
    
                    if (u == null) {
                        EmptySubscription.complete(s);
                        return;
                    }
                    s.onSubscribe(new ScalarSubscription<>(s, u));
                } else {
                    other.subscribe(s);
                }
            }
}
