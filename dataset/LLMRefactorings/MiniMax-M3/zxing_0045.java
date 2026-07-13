public class zxing_0045 {
    private static final String UTF_8 = "UTF-8";
    
      final String fillInCustomSearchURL(String text) {
        if (customProductSearch == null) {
          return text;
        }
        try {
          text = URLEncoder.encode(text, UTF_8);
        } catch (UnsupportedEncodingException e) {
        }
        String url = customProductSearch;
        if (rawResult != null) {
          url = url.replaceFirst("%f(?![0-9a-f])", rawResult.getBarcodeFormat().toString());
          if (url.contains("%t")) {
            ParsedResult parsedResultAgain = ResultParser.parseResult(rawResult);
            url = url.replace("%t", parsedResultAgain.getType().toString());
          }
        }
        return url.replace("%s", text);
      }
}
