public class zxing_0176 {

      private static List<URI> expand(Iterable<URI> inputs) throws IOException {
        List<URI> expanded = new ArrayList<>();
        for (URI input : inputs) {
          if (isFileOrDir(input)) {
            Path inputPath = Paths.get(input);
            if (Files.isDirectory(inputPath)) {
              try (DirectoryStream<Path> childPaths = Files.newDirectoryStream(inputPath)) {
                for (Path childPath : childPaths) {
                  expanded.add(childPath.toUri());
                }
              }
            } else {
              expanded.add(input);
            }
          } else {
            expanded.add(input);
          }
        }
        ensureUriScheme(expanded);
        return expanded;
      }

      private static void ensureUriScheme(List<URI> uris) {
        for (int i = 0; i < uris.size(); i++) {
          URI uri = uris.get(i);
          if (uri.getScheme() == null) {
            uris.set(i, Paths.get(uri.getRawPath()).toUri());
          }
        }
      }
}
