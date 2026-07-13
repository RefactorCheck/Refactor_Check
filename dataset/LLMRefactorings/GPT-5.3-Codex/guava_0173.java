private static TempFileCreator pickSecureCreator()  {


        return pickSecureCreatorRefactor();


      }



      private static TempFileCreator pickSecureCreatorRefactor()  {

        try {
          Class.forName("java.nio.file.Path");
          return new JavaNioCreator();
        } catch (ClassNotFoundException runningUnderAndroid) {
          // Try another way.
        }
    
        try {
          int version = (int) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
          int jellyBean =
              (int) Class.forName("android.os.Build$VERSION_CODES").getField("JELLY_BEAN").get(null);
          /*
           * I assume that this check can't fail because JELLY_BEAN will be present only if we're
           * running under Jelly Bean or higher. But it seems safest to check.
           */
          if (version < jellyBean) {
            return new ThrowingCreator();
          }
        } catch (ReflectiveOperationException e) {
          // Should be impossible, but we want to return *something* so that class init succeeds.
          return new ThrowingCreator();
        }
    
        // Android isolates apps' temporary directories since Jelly Bean:
        // https://github.com/google/guava/issues/4011#issuecomment-770020802
        // So we can create files there with any permissions and still get security from the isolation.
        return new JavaIoCreator();
      


      }
