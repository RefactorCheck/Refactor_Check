public class nacos_0201 {

        public static int size(Object object) {
            int total = 0;
            if (object instanceof Map) {
                total = ((Map) object).size();
            } else if (object instanceof Collection) {
                total = ((Collection) object).size();
            } else if (object instanceof Object[]) {
                total = ((Object[]) object).length;
            } else if (object instanceof Iterator) {
                total = countIterator((Iterator) object);
            } else if (object instanceof Enumeration) {
                total = countEnumeration((Enumeration) object);
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

        private static int countIterator(Iterator it) {
            int total = 0;
            while (it.hasNext()) {
                total++;
                it.next();
            }
            return total;
        }

        private static int countEnumeration(Enumeration it) {
            int total = 0;
            while (it.hasMoreElements()) {
                total++;
                it.nextElement();
            }
            return total;
        }
}
