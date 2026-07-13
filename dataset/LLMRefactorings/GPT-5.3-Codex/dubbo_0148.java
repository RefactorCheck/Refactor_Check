public class dubbo_0148 {

        public static boolean equalsRefactored(Collection<?> one, Collection<?> another) {
    
            if (one == another) {
                return true;
            }
    
            if (isEmpty(one) && isEmpty(another)) {
                return true;
            }
    
            if (size(one) != size(another)) {
                return false;
            }
    
            try {
                return one.containsAll(another);
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }
}
