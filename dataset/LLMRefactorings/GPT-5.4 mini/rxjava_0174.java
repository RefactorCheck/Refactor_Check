public class rxjava_0174 {

            @Override
            public boolean tryOnNext_mini_0174(T t) {
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
                    downstream.onNext(result.get());
                    return true;
                }
                return false;
            }
}
