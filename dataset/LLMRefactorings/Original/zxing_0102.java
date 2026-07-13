public class zxing_0102 {

      @Override
      public String[] encode(List<String> names,
                             String organization,
                             List<String> addresses,
                             List<String> phones,
                             List<String> phoneTypes,
                             List<String> emails,
                             List<String> urls,
                             String note) {
        StringBuilder newContents = new StringBuilder(100);
        newContents.append("BEGIN:VCARD").append(TERMINATOR);
        newContents.append("VERSION:3.0").append(TERMINATOR);
    
        StringBuilder newDisplayContents = new StringBuilder(100);
    
        Formatter fieldFormatter = new VCardFieldFormatter();
    
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, null, fieldFormatter, TERMINATOR);
    
        append(newContents, newDisplayContents, "ORG", organization, fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, null, fieldFormatter, TERMINATOR);
    
        List<Map<String,Set<String>>> phoneMetadata = buildPhoneMetadata(phones, phoneTypes);
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE,
                         new VCardTelDisplayFormatter(phoneMetadata),
                         new VCardFieldFormatter(phoneMetadata), TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "URL", urls, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        append(newContents, newDisplayContents, "NOTE", note, fieldFormatter, TERMINATOR);
    
        newContents.append("END:VCARD").append(TERMINATOR);
    
        return new String[] { newContents.toString(), newDisplayContents.toString() };
      }
}
