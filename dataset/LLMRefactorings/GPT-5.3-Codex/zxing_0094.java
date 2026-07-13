public class zxing_0094 {

      private static void translateOneLanguageRefactored(Path assetsDir,
                                               String language,
                                               final Collection<String> filesToTranslate) throws IOException {
        Path targetHtmlDir = assetsDir.resolve("html-" + language);
        Files.createDirectories(targetHtmlDir);
        Path englishHtmlDir = assetsDir.resolve("html-en");
    
        String translationTextTranslated =
            StringsResourceTranslator.translateString("Translated by Google Translate.", language);
    
        DirectoryStream.Filter<Path> filter = entry -> {
          String name = entry.getFileName().toString();
          return name.endsWith(".html") && (filesToTranslate.isEmpty() || filesToTranslate.contains(name));
        };
        try (DirectoryStream<Path> files = Files.newDirectoryStream(englishHtmlDir, filter)) {
          for (Path sourceFile : files) {
            translateOneFile(language, targetHtmlDir, sourceFile, translationTextTranslated);
          }
        }
      }
}
