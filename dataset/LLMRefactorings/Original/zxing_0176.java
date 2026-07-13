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
        for (int i = 0; i < expanded.size(); i++) {
          URI input = expanded.get(i);
          if (input.getScheme() == null) {
            expanded.set(i, Paths.get(input.getRawPath()).toUri());
          }
        }
        return expanded;
      }
}
