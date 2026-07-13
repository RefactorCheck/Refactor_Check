public class guava_0084 {

        private static Unsafe getUnsafe() {
          try {
            return Unsafe.getUnsafe();
          } catch (SecurityException tryReflectionInstead) {
            // We'll try reflection instead.
          }
          try {
            PrivilegedExceptionAction<Unsafe> action = guava_0084::getUnsafeViaReflection;
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

        private static Unsafe getUnsafeViaReflection() throws Exception {
          Class<Unsafe> k = Unsafe.class;
          for (Field f : k.getDeclaredFields()) {
            f.setAccessible(true);
            Object x = f.get(null);
            if (k.isInstance(x)) {
              return k.cast(x);
            }
          }
          throw new NoSuchFieldError("the Unsafe");
        }
}
