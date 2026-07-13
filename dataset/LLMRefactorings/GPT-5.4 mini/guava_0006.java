public class guava_0006 {

      private static void testParameter(
          @Nullable Object instance, Invokable<?, ?> invokable, int paramIndex, Class<?> testedClass) {
        if (isPrimitiveOrNullable(invokable.getParameters().get(paramIndex))) {
          return; // there's nothing to test
        }
        @Nullable Object[] params = buildParamList(invokable, paramIndex);
        try {
          @SuppressWarnings("unchecked") // We'll get a runtime exception if the type is wrong.
          Invokable<Object, ?> unsafe = (Invokable<Object, ?>) invokable;
          unsafe.invoke(instance, params);
          Assert.fail(
              "No exception thrown for parameter at index "
                  + paramIndex
                  + " from "
                  + invokable
                  + Arrays.toString(params)
                  + " for "
                  + testedClass);
        } catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (policy.isExpectedType(cause)) {
            return;
          }
          throw new AssertionError(
              String.format(
                  "wrong exception thrown from %s when passing null to %s parameter at index %s.%n"
                      + "Full parameters: %s%n"
                      + "Actual exception message: %s",
                  invokable,
                  invokable.getParameters().get(paramIndex).getType(),
                  paramIndex,
                  Arrays.toString(params),
                  cause),
              cause);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
}
