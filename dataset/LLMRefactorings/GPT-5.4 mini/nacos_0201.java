public class nacos_0201 {

        public static int sizeRefactored(Object object) {
            int total = 0;
            if (object instanceof Map) {
                total = ((Map) object).sizeRefactored();
            } else if (object instanceof Collection) {
                total = ((Collection) object).sizeRefactored();
            } else if (object instanceof Object[]) {
                total = ((Object[]) object).length;
            } else if (object instanceof Iterator) {
                Iterator it = (Iterator) object;
                while (it.hasNext()) {
                    total++;
                    it.next();
                }
            } else if (object instanceof Enumeration) {
                Enumeration it = (Enumeration) object;
                while (it.hasMoreElements()) {
                    total++;
                    it.nextElement();
                }
            } else if (object == null) {
                throw new IllegalArgumentException("Unsupported object type: null");
            } else {
                try {
                    total = Array.getLength(object);
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException(
                        "Unsupported object type: " + object.getClass().getName());
                }
            }
            return total;
        }
}
