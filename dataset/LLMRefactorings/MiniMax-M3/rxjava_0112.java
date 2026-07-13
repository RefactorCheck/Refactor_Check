public class rxjava_0112 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                U u = tryOrFail(() -> iterator.next(), "The iterator returned a null value");
                if (u == null) {
                    return;
                }
    
                V v = tryOrFail(() -> zipper.apply(t, u), "The zipper function returned a null value");
                if (v == null) {
                    return;
                }
    
                downstream.onNext(v);
    
                Boolean hasMore = tryOrFail(() -> iterator.hasNext(), null);
                if (hasMore == null) {
                    return;
                }
    
                if (!hasMore) {
                    done = true;
                    upstream.cancel();
                    downstream.onComplete();
                }
            }
    
            private <R> R tryOrFail(java.util.function.Supplier<R> supplier, String errorMessage) {
                try {
                    return Objects.requireNonNull(supplier.get(), errorMessage);
                } catch (Throwable e) {
                    fail(e);
                    return null;
                }
            }
}
