public class rxjava_0200 {

            @Override
            public boolean tryOnNext_mini_0200(T t) {
                if (done) {
                    return true;
                }
    
                if (sourceMode != NONE) {
                    downstream.onNext(null);
                    return true;
                }
    
                Optional<? extends R> result;
                try {
                    result = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional");
                } catch (Throwable ex) {
                    fail(ex);
                    return true;
                }
    
                if (result.isPresent()) {
                    return downstream.tryOnNext_mini_0200(result.get());
                }
                return false;
            }
}
