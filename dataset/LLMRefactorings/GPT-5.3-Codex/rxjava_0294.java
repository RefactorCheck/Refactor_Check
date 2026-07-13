public class rxjava_0294 {

        @SuppressWarnings("unchecked")
        @NonNull
        public final U assertValueAt(final int index, @NonNull Predicate<T> valuePredicate) {
            int s = values.size();
            if (s == 0) {
                throw fail("No values");
            }
    
            if (index < 0 || index >= s) {
                throw fail("Index " + index + " is out of range [0, " + s + ")");
            }
    
            boolean found = false;
    
            T v = values.get(index);
            try {
                if (valuePredicate.test(v)) {
                    found = true;
                }
            } catch (Throwable ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
    
            if (!found) {
                throw fail("Value " + valueAndClass(v) + " at position " + index + " did not pass the predicate");
            }
            return (U)this;
        }
}
