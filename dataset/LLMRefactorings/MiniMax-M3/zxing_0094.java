public class zxing_0094 {

      private static void translateOneLanguage(Path assetsDir,
                                               String language,
                                               final Collection<String> filesToTranslate) throws IOException {
        Path targetHtmlDir = assetsDir.resolve("html-" + language);
        Files.createDirectories(targetHtmlDir);
        Path englishHtmlDir = assetsDir.resolve("html-en");
    
        String translationTextTranslated =
            StringsResourceTranslator.translateString("Translated by Google Translate.", language);
    
        try (DirectoryStream<Path> files = Files.newDirectoryStream(englishHtmlDir, createHtmlFilter(filesToTranslate))) {
          for (Path sourceFile : files) {
            translateOneFile(language, targetHtmlDir, sourceFile, translationTextTranslated);
          }
        }
      }

      private static DirectoryStream.Filter<Path> createHtmlFilter(final Collection<String> filesToTranslate) {
        return entry -> {
          String name = entry.getFileName().toString();
          return name.endsWith(".html") && (filesToTranslate.isEmpty() || filesToTranslate.contains(name));
        };
      }
}
