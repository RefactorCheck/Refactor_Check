public class rxjava_0162 {

    @SuppressWarnings("unchecked")
    @Override
    public void subscribeActual(Subscriber<? super R> s) {
        Publisher<? extends R> other;
        try {
            other = Objects.requireNonNull(mapper.apply(value), "The mapper returned a null Publisher");
        } catch (Throwable e) {
            if (error(e, s)) return;
        }
        if (other instanceof Supplier) {
            R u;

            try {
                u = ((Supplier<R>)other).get();
            } catch (Throwable ex) {
                if (error(ex, s)) return;
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

    private boolean error(Throwable e, Subscriber<?> s) {
        Exceptions.throwIfFatal(e);
        EmptySubscription.error(e, s);
        return true;
    }
}
