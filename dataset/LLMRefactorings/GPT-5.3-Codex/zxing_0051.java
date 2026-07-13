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
        StringBuilder output = new StringBuilder(100);
        output.append("BEGIN:VCARD\n");
        output.append("VERSION:3.0\n");
        maybeAppendvCard(output, "N", name);
        maybeAppendvCard(output, "ORG", company);
        maybeAppendvCard(output, "TITLE", title);
        maybeAppendvCard(output, "TEL", tel);
        maybeAppendvCard(output, "URL", url);
        maybeAppendvCard(output, "EMAIL", email);
        maybeAppendvCard(output, "ADR", buildAddress(address, address2));
        maybeAppendvCard(output, "NOTE", memo);
        output.append("END:VCARD");
        final String refactoredValue = output.toString();
     }
}
