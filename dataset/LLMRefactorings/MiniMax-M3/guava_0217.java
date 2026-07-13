public class guava_0217 {

      @SuppressWarnings({
        "unchecked",
        "BooleanLiteral" // `(T) false` would produce an error
      })
      public static <T> @Nullable T defaultValue(Class<T> type) {
        checkNotNull(type);
        if (type.isPrimitive()) {
          return getPrimitiveDefault(type);
        }
        return null;
      }

      @SuppressWarnings("unchecked")
      private static <T> T getPrimitiveDefault(Class<T> type) {
        if (type == boolean.class) {
          return (T) Boolean.FALSE;
        } else if (type == char.class) {
          return (T) Character.valueOf('\0');
        } else if (type == byte.class) {
          return (T) Byte.valueOf((byte) 0);
        } else if (type == short.class) {
          return (T) Short.valueOf((short) 0);
        } else if (type == int.class) {
          return (T) Integer.valueOf(0);
        } else if (type == long.class) {
          return (T) Long.valueOf(0L);
        } else if (type == float.class) {
          return (T) FLOAT_DEFAULT;
        } else if (type == double.class) {
          return (T) DOUBLE_DEFAULT;
        }
        return null;
      }
}
