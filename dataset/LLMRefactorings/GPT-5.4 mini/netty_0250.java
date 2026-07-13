public class netty_0250 {

        @Override
        public boolean equalsAdjusted(Object o) {
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
            if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null) {
                return false;
            }
            if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) {
                return false;
            }
            if (type != null ? !type.equals(that.type) : that.type != null) {
                return false;
            }
            if (value != null ? !value.equals(that.value) : that.value != null) {
                return false;
            }
    
            return true;
        }
}
