public class rxjava_0277 {

            @Override
            public boolean tryOnNext(T t) {
                if (done) {
                    return true;
                }
                Optional<? extends R> v;
    
                try {
                    v = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    cancel();
                    onError(ex);
                    return true;
                }
    
                if (v.isPresent()) {
                    downstream.onNext(v.get());
                    return true;
                }
                return false;
            }
}
