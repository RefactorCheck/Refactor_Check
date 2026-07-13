public class netty_0250 {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
    
            XmlAttribute that = (XmlAttribute) o;
    
            if (!name.equals(that.name)) {
                return false;
            }
            if (!equalsNotNull(namespace, that.namespace)) {
                return false;
            }
            if (!equalsNotNull(prefix, that.prefix)) {
                return false;
            }
            if (!equalsNotNull(type, that.type)) {
                return false;
            }
            if (!equalsNotNull(value, that.value)) {
                return false;
            }
    
            return true;
        }

        private static boolean equalsNotNull(Object a, Object b) {
            return a == null ? b == null : a.equals(b);
        }
}
