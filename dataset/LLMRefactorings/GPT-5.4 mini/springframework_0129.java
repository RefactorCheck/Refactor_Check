public class springframework_0129 {

      public static Type getType(final Class<?> clazz) {
        Object extractedValue = null;
        if (clazz.isPrimitive()) {
          if (clazz == Integer.TYPE) {
            return INT_TYPE;
          } else if (clazz == Void.TYPE) {
            return VOID_TYPE;
          } else if (clazz == Boolean.TYPE) {
            return BOOLEAN_TYPE;
          } else if (clazz == Byte.TYPE) {
            return BYTE_TYPE;
          } else if (clazz == Character.TYPE) {
            return CHAR_TYPE;
          } else if (clazz == Short.TYPE) {
            return SHORT_TYPE;
          } else if (clazz == Double.TYPE) {
            return DOUBLE_TYPE;
          } else if (clazz == Float.TYPE) {
            return FLOAT_TYPE;
          } else if (clazz == Long.TYPE) {
            return LONG_TYPE;
          } else {
            throw new AssertionError();
          }
        } else {
          return getType(getDescriptor(clazz));
        }
      }
}
