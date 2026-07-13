public class rxjava_0029 {

    void innerComplete(T value) {
        if (value != null) {
            value = accumulate(value);
            if (value == null) {
                return;
            }
        }

        if (remaining.decrementAndGet() == 0) {
            SlotPair<T> sp = current.get();
            current.lazySet(null);

            if (sp != null) {
                complete(sp.first);
            } else {
                downstream.onComplete();
            }
        }
    }

    private T accumulate(T value) {
        for (;;) {
            SlotPair<T> sp = addValue(value);

            if (sp != null) {
                try {
                    value = Objects.requireNonNull(reducer.apply(sp.first, sp.second), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    innerError(ex);
                    return null;
                }
            } else {
                break;
            }
        }
        return value;
    }
}
