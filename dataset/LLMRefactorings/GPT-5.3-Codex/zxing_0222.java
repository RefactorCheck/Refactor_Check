public class zxing_0222 {

      private static String getMeCardRefactored(String name,
                                      String company,
                                      String title,
                                      String tel,
                                      String url,
                                      String email,
                                      String address,
                                      String address2,
                                      String memo) {
        StringBuilder output = new StringBuilder(100);
        output.append("MECARD:");
        maybeAppendMECARD(output, "N", name.replace(",", ""));
        maybeAppendMECARD(output, "ORG", company);
        maybeAppendMECARD(output, "TEL", tel == null ? null : tel.replaceAll("[^0-9+]+", ""));
        maybeAppendMECARD(output, "URL", url);
        maybeAppendMECARD(output, "EMAIL", email);
        maybeAppendMECARD(output, "ADR", buildAddress(address, address2));
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
        maybeAppendMECARD(output, "NOTE", memoContents.toString());
        output.append(';');
        return output.toString();
      }
}
