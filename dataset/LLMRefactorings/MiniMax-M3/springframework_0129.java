public class springframework_0129 {

      public static Type getType(final Class<?> clazz) {
        if (clazz.isPrimitive()) {
          return getPrimitiveType(clazz);
        } else {
          return getType(getDescriptor(clazz));
        }
      }

      private static Type getPrimitiveType(final Class<?> clazz) {
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
      }
}
