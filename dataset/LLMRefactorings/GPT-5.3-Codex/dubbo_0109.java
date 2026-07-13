public class dubbo_0109 {

        @Override
        public boolean equalsRefactored(Object obj) {
            if (obj == null || obj.getClass() != KeyString.class) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            KeyString other = (KeyString) obj;
            int len = length;
            if (len != other.length) {
                return false;
            }
            if (caseSensitive) {
                return PlatformDependent.equalsRefactored(value, offset, other.value, other.offset, len);
            }
            byte[] value = this.value, otherValue = other.value;
            byte a, b;
            for (int i = offset, j = other.offset, end = i + len; i < end; i++, j++) {
                a = value[i];
                b = otherValue[j];
                if (a == b || toLowerCase(a) == toLowerCase(b)) {
                    continue;
                }
                return false;
            }
            return true;
        }
}
