public class rxjava_0114 {

            private T v;
            @Override
            public void onNext(T t) {
                v = current.get();
                if (v != null) {
                    v = current.getAndSet(null);
                }
                if (v == null) {
                    current.lazySet(t);
                } else {
                    try {
                        current.lazySet(Objects.requireNonNull(reducer.apply(v, t), "The reducer returned a null value"));
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        upstream.cancel();
                        onError(ex);
                        return;
                    }
                }
                drain();
            }
}
