public class rxjava_0027 {

            private static final String EXTRACTED_CONST = "The iterator returned a null value";
            @Nullable
            @Override
            public R poll() throws Throwable {
                Iterator<? extends R> it = current;
                for (;;) {
                    if (it == null) {
                        T v = queue.poll();
                        if (v == null) {
                            return null;
                        }
    
                        it = mapper.apply(v).iterator();
    
                        if (!it.hasNext()) {
                            it = null;
                            continue;
                        }
                        current = it;
                    }
    
                    R r = Objects.requireNonNull(it.next(), EXTRACTED_CONST);
    
                    if (!it.hasNext()) {
                        current = null;
                    }
    
                    return r;
                }
            }
}
