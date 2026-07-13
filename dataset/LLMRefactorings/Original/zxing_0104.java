public class zxing_0104 {

      static String translateString(String english, String language) throws IOException {
        if ("en".equals(language)) {
          return english;
        }
        String massagedLanguage = LANGUAGE_CODE_MASSAGINGS.get(language);
        if (massagedLanguage != null) {
          language = massagedLanguage;
        }
        System.out.println("  Need translation for " + english);
    
        URI translateURI = URI.create(
            "https://www.googleapis.com/language/translate/v2?key=" + API_KEY + "&q=" +
                URLEncoder.encode(english, "UTF-8") +
                "&source=en&target=" + language);
        CharSequence translateResult = fetch(translateURI);
        Matcher m = TRANSLATE_RESPONSE_PATTERN.matcher(translateResult);
        if (!m.find()) {
          System.err.println("No translate result");
          System.err.println(translateResult);
          return english;
        }
        String translation = m.group(1);
    
        // This is a little crude; unescape some common escapes in the raw response
        translation = translation.replaceAll("&(amp;)?quot;", "\"");
        translation = translation.replaceAll("&(amp;)?#39;", "'");
    
        System.out.println("  Got translation " + translation);
        return translation;
      }
}
