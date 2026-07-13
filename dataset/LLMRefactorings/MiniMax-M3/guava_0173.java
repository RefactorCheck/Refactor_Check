public class guava_0173 {

      private static TempFileCreator pickSecureCreator() {
        try {
          Class.forName("java.nio.file.Path");
          return new JavaNioCreator();
        } catch (ClassNotFoundException runningUnderAndroid) {
          // Try another way.
        }

        if (runningOnAndroidJellyBeanOrHigher()) {
          // Android isolates apps' temporary directories since Jelly Bean:
          // https://github.com/google/guava/issues/4011#issuecomment-770020802
          // So we can create files there with any permissions and still get security from the isolation.
          return new JavaIoCreator();
        }
        return new ThrowingCreator();
      }

      private static boolean runningOnAndroidJellyBeanOrHigher() {
        try {
          int version = (int) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
          int jellyBean =
              (int) Class.forName("android.os.Build$VERSION_CODES").getField("JELLY_BEAN").get(null);
          /*
           * I assume that this check can't fail because JELLY_BEAN will be present only if we're
           * running under Jelly Bean or higher. But it seems safest to check.
           */
          return version >= jellyBean;
        } catch (ReflectiveOperationException e) {
          // Should be impossible, but we want to return *something* so that class init succeeds.
          return false;
        }
      }
}
