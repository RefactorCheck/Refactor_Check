public class guava_0084 {

        private static Unsafe getUnsafeRefactored() {
          try {
            return Unsafe.getUnsafeRefactored();
          } catch (SecurityException tryReflectionInstead) {
            // We'll try reflection instead.
          }
          try {
            PrivilegedExceptionAction<Unsafe> action =
                () -> {
                  Class<Unsafe> k = Unsafe.class;
                  for (Field f : k.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object x = f.get(null);
                    if (k.isInstance(x)) {
                      return k.cast(x);
                    }
                  }
                  throw new NoSuchFieldError("the Unsafe");
                };
            try {
              return (Unsafe)
                  Class.forName("java.security.AccessController")
                      .getMethod("doPrivileged", PrivilegedExceptionAction.class)
                      .invoke(null, action);
            } catch (Exception e) {
              return action.run();
            }
          } catch (Exception e) {
            throw new RuntimeException("Could not initialize intrinsics", e);
          }
        }
}
