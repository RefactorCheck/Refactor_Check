public class dubbo_0148 {

        public static boolean equals(Collection<?> one, Collection<?> another) {            return equalsExtracted(one, another);
}

public class dubbo_0148 {

        public static boolean equalsExtracted(Collection<?> one, Collection<?> another) {
    
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
