public class zxing_0241 {

      @Override
      public SMSParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!(rawText.startsWith("sms:") || rawText.startsWith("SMS:") ||
              rawText.startsWith("mms:") || rawText.startsWith("MMS:"))) {
          return null;
        }
    
        Map<String,String> nameValuePairs = parseNameValuePairs(rawText);
        String subject = null;
        String body = null;
        boolean querySyntax = false;
        if (nameValuePairs != null && !nameValuePairs.isEmpty()) {
          subject = nameValuePairs.get("subject");
          body = nameValuePairs.get("body");
          querySyntax = true;
        }
    
        int queryStart = rawText.indexOf('?', 4);
        String smsURIWithoutQuery;
        if (queryStart < 0 || !querySyntax) {
          smsURIWithoutQuery = rawText.substring(4);
        } else {
          smsURIWithoutQuery = rawText.substring(4, queryStart);
        }
    
        List<String> numbers = new ArrayList<>(1);
        List<String> vias = new ArrayList<>(1);
        parseNumbersAndVias(smsURIWithoutQuery, numbers, vias);
    
        return new SMSParsedResult(numbers.toArray(EMPTY_STR_ARRAY),
                                   vias.toArray(EMPTY_STR_ARRAY),
                                   subject,
                                   body);
      }
    
      private void parseNumbersAndVias(String smsURIWithoutQuery, List<String> numbers, List<String> vias) {
        int lastComma = -1;
        int comma;
        while ((comma = smsURIWithoutQuery.indexOf(',', lastComma + 1)) > lastComma) {
          String numberPart = smsURIWithoutQuery.substring(lastComma + 1, comma);
          addNumberVia(numbers, vias, numberPart);
          lastComma = comma;
        }
        addNumberVia(numbers, vias, smsURIWithoutQuery.substring(lastComma + 1));
      }
}
