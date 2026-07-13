public class zxing_0051 {

      private static String getVCard(String name,
                                     String company,
                                     String title,
                                     String tel,
                                     String url,
                                     String email,
                                     String address,
                                     String address2,
                                     String memo) {
        StringBuilder outputRefactored = new StringBuilder(100);
        outputRefactored.append("BEGIN:VCARD\n");
        outputRefactored.append("VERSION:3.0\n");
        maybeAppendvCard(outputRefactored, "N", name);
        maybeAppendvCard(outputRefactored, "ORG", company);
        maybeAppendvCard(outputRefactored, "TITLE", title);
        maybeAppendvCard(outputRefactored, "TEL", tel);
        maybeAppendvCard(outputRefactored, "URL", url);
        maybeAppendvCard(outputRefactored, "EMAIL", email);
        maybeAppendvCard(outputRefactored, "ADR", buildAddress(address, address2));
        maybeAppendvCard(outputRefactored, "NOTE", memo);
        outputRefactored.append("END:VCARD");
        return outputRefactored.toString();
      }
}
