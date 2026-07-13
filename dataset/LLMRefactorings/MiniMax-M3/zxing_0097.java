public class zxing_0097 {

      @Override
      public EmailAddressParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (rawText.startsWith("mailto:") || rawText.startsWith("MAILTO:")) {
          String hostEmail = parseHostEmail(rawText);
          if (hostEmail == null) {
            return null;
          }
          String[] tos = null;
          if (!hostEmail.isEmpty()) {
            tos = COMMA.split(hostEmail);
          }
          Map<String,String> nameValues = parseNameValuePairs(rawText);
          String[] ccs = null;
          String[] bccs = null;
          String subject = null;
          String body = null;
          if (nameValues != null) {
            if (tos == null) {
              String tosString = nameValues.get("to");
              if (tosString != null) {
                tos = COMMA.split(tosString);
              }
            }
            String ccString = nameValues.get("cc");
            if (ccString != null) {
              ccs = COMMA.split(ccString);
            }
            String bccString = nameValues.get("bcc");
            if (bccString != null) {
              bccs = COMMA.split(bccString);
            }
            subject = nameValues.get("subject");
            body = nameValues.get("body");
          }
          return new EmailAddressParsedResult(tos, ccs, bccs, subject, body);
        } else {
          if (!EmailDoCoMoResultParser.isBasicallyValidEmailAddress(rawText)) {
            return null;
          }
          return new EmailAddressParsedResult(rawText);
        }
      }

      private String parseHostEmail(String rawText) {
          String hostEmail = rawText.substring(7);
          int queryStart = hostEmail.indexOf('?');
          if (queryStart >= 0) {
            hostEmail = hostEmail.substring(0, queryStart);
          }
          try {
            return urlDecode(hostEmail);
          } catch (IllegalArgumentException iae) {
            return null;
          }
      }
}
