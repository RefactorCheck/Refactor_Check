public class zxing_0051 {

      private static final String VCARD_BEGIN = "BEGIN:VCARD\n";
      private static final String VCARD_VERSION = "VERSION:3.0\n";
      private static final String VCARD_END = "END:VCARD";

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
        output.append(VCARD_BEGIN);
        output.append(VCARD_VERSION);
        maybeAppendvCard(output, "N", name);
        maybeAppendvCard(output, "ORG", company);
        maybeAppendvCard(output, "TITLE", title);
        maybeAppendvCard(output, "TEL", tel);
        maybeAppendvCard(output, "URL", url);
        maybeAppendvCard(output, "EMAIL", email);
        maybeAppendvCard(output, "ADR", buildAddress(address, address2));
        maybeAppendvCard(output, "NOTE", memo);
        output.append(VCARD_END);
        return output.toString();
      }
}
