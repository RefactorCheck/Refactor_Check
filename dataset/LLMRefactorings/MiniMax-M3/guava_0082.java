public class guava_0082 {

      private static <X> @Nullable X newFromConstructor(Constructor<X> constructor, Throwable cause) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
          Object param = getParamValue(paramTypes[i], cause);
          if (param == null) {
            return null;
          }
          params[i] = param;
        }
        try {
          return constructor.newInstance(params);
        } catch (IllegalArgumentException
            | InstantiationException
            | IllegalAccessException
            | InvocationTargetException e) {
          return null;
        }
      }

      private static Object getParamValue(Class<?> paramType, Throwable cause) {
        if (paramType.equals(String.class)) {
          return cause.toString();
        }
        if (paramType.equals(Throwable.class)) {
          return cause;
        }
        return null;
      }
}
