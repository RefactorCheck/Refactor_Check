public class guava_0186 {

      @VisibleForTesting
      static ImmutableSet<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
        if (manifest == null) {
          return ImmutableSet.of();
        }
        ImmutableSet.Builder<File> builder = ImmutableSet.builder();
        String classpathAttribute =
            manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute != null) {
          for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
            addClassPathEntry(builder, jarFile, path);
          }
        }
        return builder.build();
      }

      private static void addClassPathEntry(ImmutableSet.Builder<File> builder, File jarFile, String path) {
        URL url;
        try {
          url = getClassPathEntry(jarFile, path);
        } catch (MalformedURLException e) {
          logger.warning("Invalid Class-Path entry: " + path);
          return;
        }
        if (url.getProtocol().equals("file")) {
          builder.add(toFile(url));
        }
      }
}
