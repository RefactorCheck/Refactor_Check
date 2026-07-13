public class zxing_0211 {

      private static Path buildOutputPath(URI input, String suffix) throws IOException {
        Path outDir;
        String inputFileName;
        if ("file".equals(input.getScheme())) {
          Path inputPath = Paths.get(input);
          outDir = inputPath.getParent();
          inputFileName = inputPath.getFileName().toString();
        } else {
          outDir = Paths.get(".").toRealPath();
          String path = input.getPath();
          if (path == null) {
            inputFileName = "input";
          } else {
            String[] pathElements = path.split("/");
            inputFileName = pathElements[pathElements.length - 1];
          }
        }
    
        // Replace/add extension
        int pos = inputFileName.lastIndexOf('.');
        if (pos > 0) {
          inputFileName = inputFileName.substring(0, pos) + suffix;
        } else {
          inputFileName += suffix;
        }
    
        return outDir.resolve(inputFileName);
      }
}
