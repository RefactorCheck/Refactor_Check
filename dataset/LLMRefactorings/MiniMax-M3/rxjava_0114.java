public class rxjava_0114 {

            @Override
            public void onNext(T t) {
                T v = current.get();
                if (v != null) {
                    v = current.getAndSet(null);
                }
                if (v == null) {
                    current.lazySet(t);
                } else if (!applyReducer(v, t)) {
                    return;
                }
                drain();
            }

            private boolean applyReducer(T v, T t) {
                try {
                    current.lazySet(Objects.requireNonNull(reducer.apply(v, t), "The reducer returned a null value"));
                    return true;
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    onError(ex);
                    return false;
                }
            }
}
