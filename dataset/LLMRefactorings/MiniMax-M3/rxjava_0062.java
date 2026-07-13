public class rxjava_0062 {

    @Override
    public boolean tryOnNext(T t) {
        if (!done) {
            boolean matches;

            try {
                matches = predicate.test(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                cancel();
                onError(ex);
                return false;
            }

            if (matches) {
                return downstream.tryOnNext(t);
            }
        }
        return false;
    }
}
