public class guava_0144 {

        @Override
        File createTempDir() {
          File baseDir = new File(JAVA_IO_TMPDIR.value());
          @SuppressWarnings("GoodTime") // reading system time without TimeSource
          String baseName = System.currentTimeMillis() + "-";
    
          for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
              return tempDir;
            }
          }
          throw new IllegalStateException(
              "Failed to create directory within "
                  + TEMP_DIR_ATTEMPTS
                  + " attempts (tried "
                  + baseName
                  + "0 to "
                  + baseName
                  + (TEMP_DIR_ATTEMPTS - 1)
                  + ')');
        }
}
