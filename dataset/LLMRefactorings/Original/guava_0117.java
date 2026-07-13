public class guava_0117 {

        @Override
        public @Nullable Object invoke(Object proxy, Method method, @Nullable Object @Nullable [] args)
            throws Throwable {
          String methodName = method.getName();
          Method typeVariableMethod = typeVariableMethods.get(methodName);
          if (typeVariableMethod == null) {
            if (methodName.equals("getAnnotatedBounds")
                || methodName.equals("isAnnotationPresent")
                // Each of these prefixes is shared by a family of methods:
                || methodName.startsWith("getAnnotation")
                || methodName.startsWith("getDeclaredAnnotation")) {
              throw new UnsupportedOperationException(
                  "Annotation methods are not supported on synthetic TypeVariables created during type"
                      + " resolution. The semantics of annotations on resolved types with modified"
                      + " bounds are undefined. Use the original TypeVariable for annotation access."
                      + " See b/147144588.");
            }
            // If any other method appears or if we forgot one, include it in the exception message:
            throw new UnsupportedOperationException(methodName);
          } else {
            try {
              return typeVariableMethod.invoke(typeVariableImpl, args);
            } catch (InvocationTargetException e) {
              throw e.getCause();
            }
          }
        }
}
