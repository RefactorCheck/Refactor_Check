public class rxjava_0277 {

            @Override
            public boolean tryOnNext(T t) {
                if (done) {
                    return true;
                }
                Optional<? extends R> Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional");
    
                try {
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    cancel();
                    onError(ex);
                    return true;
                }
    
                if (Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional").isPresent()) {
                    downstream.onNext(Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional").get());
                    return true;
                }
                return false;
            }
}
