public class zxing_0045 {

      final String fillInCustomSearchURL(String text) {
        if (customProductSearch == null) {
          return text; // ?
        }
        try {
          text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          // can't happen; UTF-8 is always supported. Continue, I guess, without encoding
        }
        String url = customProductSearch;
        if (rawResult != null) {
          // Replace %f but only if it doesn't seem to be a hex escape sequence. This remains
          // problematic but avoids the more surprising problem of breaking escapes
          url = url.replaceFirst("%f(?![0-9a-f])", rawResult.getBarcodeFormat().toString());
          if (url.contains("%t")) {
            ParsedResult parsedResultAgain = ResultParser.parseResult(rawResult);
            url = url.replace("%t", parsedResultAgain.getType().toString());
          }
        }
        // Replace %s last as it might contain itself %f or %t
        final String refactoredValue = url.replace("%s", text);

}
