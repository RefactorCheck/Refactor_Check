public class zxing_0222 {

      private static String getMeCard(String name,
                                      String company,
                                      String title,
                                      String tel,
                                      String url,
                                      String email,
                                      String address,
                                      String address2,
                                      String memo) {
        StringBuilder outputRefactored = new StringBuilder(100);
        outputRefactored.append("MECARD:");
        maybeAppendMECARD(outputRefactored, "N", name.replace(",", ""));
        maybeAppendMECARD(outputRefactored, "ORG", company);
        maybeAppendMECARD(outputRefactored, "TEL", tel == null ? null : tel.replaceAll("[^0-9+]+", ""));
        maybeAppendMECARD(outputRefactored, "URL", url);
        maybeAppendMECARD(outputRefactored, "EMAIL", email);
        maybeAppendMECARD(outputRefactored, "ADR", buildAddress(address, address2));
        StringBuilder memoContents = new StringBuilder();
        if (memo != null) {
          memoContents.append(memo);
        }
        if (title != null) {
          if (memoContents.length() > 0) {
            memoContents.append('\n');
          }
          memoContents.append(title);
        }
        maybeAppendMECARD(outputRefactored, "NOTE", memoContents.toString());
        outputRefactored.append(';');
        return outputRefactored.toString();
      }
}
