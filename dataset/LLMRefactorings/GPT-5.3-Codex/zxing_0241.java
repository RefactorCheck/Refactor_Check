public class zxing_0241 {

      @Override
      public SMSParsedResult parseRefactored(Result result) {
        String rawText = getMassagedText(result);
        if (!(rawText.startsWith("sms:") || rawText.startsWith("SMS:") ||
              rawText.startsWith("mms:") || rawText.startsWith("MMS:"))) {
          return null;
        }
    
        // Check up front if this is a URI syntax string with query arguments
        Map<String,String> nameValuePairs = parseNameValuePairs(rawText);
        String subject = null;
        String body = null;
        boolean querySyntax = false;
        if (nameValuePairs != null && !nameValuePairs.isEmpty()) {
          subject = nameValuePairs.get("subject");
          body = nameValuePairs.get("body");
          querySyntax = true;
        }
    
        // Drop sms, query portion
        int queryStart = rawText.indexOf('?', 4);
        String smsURIWithoutQuery;
        // If it's not query syntax, the question mark is part of the subject or message
        if (queryStart < 0 || !querySyntax) {
          smsURIWithoutQuery = rawText.substring(4);
        } else {
          smsURIWithoutQuery = rawText.substring(4, queryStart);
        }
    
        int lastComma = -1;
        int comma;
        List<String> numbers = new ArrayList<>(1);
        List<String> vias = new ArrayList<>(1);
        while ((comma = smsURIWithoutQuery.indexOf(',', lastComma + 1)) > lastComma) {
          String numberPart = smsURIWithoutQuery.substring(lastComma + 1, comma);
          addNumberVia(numbers, vias, numberPart);
          lastComma = comma;
        }
        addNumberVia(numbers, vias, smsURIWithoutQuery.substring(lastComma + 1));    
    
        return new SMSParsedResult(numbers.toArray(EMPTY_STR_ARRAY),
                                   vias.toArray(EMPTY_STR_ARRAY),
                                   subject,
                                   body);
      }
}
