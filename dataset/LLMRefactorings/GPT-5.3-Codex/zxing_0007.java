public class zxing_0007 {

      private static String parseExtension5String(String raw) {
        String currency;
        switch (raw.charAt(0)) {
          case '0':
            currency = "£";
            break;
          case '5':
            currency = "$";
            break;
          case '9':
            // Reference: http://www.jollytech.com
            switch (raw) {
              case "90000":
                // No suggested retail price
                return null;
              case "99991":
                // Complementary
                return "0.00";
              case "99990":
                return "Used";
            }
            // Otherwise... unknown currency?
            currency = "";
            break;
          default:
            currency = "";
            break;
        }
        int rawAmount = Integer.parseInt(raw.substring(1));
        String unitsString = String.valueOf(rawAmount / 100);
        int hundredths = rawAmount % 100;
        String hundredthsString = hundredths < 10 ? "0" + hundredths : String.valueOf(hundredths);
        return currency + unitsString + '.' + hundredthsString;
      }
}
