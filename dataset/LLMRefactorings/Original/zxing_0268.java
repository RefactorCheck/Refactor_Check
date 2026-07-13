public class zxing_0268 {

      public String getSMSURI() {
        StringBuilder result = new StringBuilder();
        result.append("sms:");
        boolean first = true;
        for (int i = 0; i < numbers.length; i++) {
          if (first) {
            first = false;
          } else {
            result.append(',');
          }
          result.append(numbers[i]);
          if (vias != null && vias[i] != null) {
            result.append(";via=");
            result.append(vias[i]);
          }
        }
        boolean hasBody = body != null;
        boolean hasSubject = subject != null;
        if (hasBody || hasSubject) {
          result.append('?');
          if (hasBody) {
            result.append("body=");
            result.append(body);
          }
          if (hasSubject) {
            if (hasBody) {
              result.append('&');
            }
            result.append("subject=");
            result.append(subject);
          }
        }
        return result.toString();
      }
}
