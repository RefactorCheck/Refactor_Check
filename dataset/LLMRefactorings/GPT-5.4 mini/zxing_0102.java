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
        StringBuilder newContentsRefactored = new StringBuilder(100);
        newContentsRefactored.append("BEGIN:VCARD").append(TERMINATOR);
        newContentsRefactored.append("VERSION:3.0").append(TERMINATOR);
    
        StringBuilder newDisplayContents = new StringBuilder(100);
    
        Formatter fieldFormatter = new VCardFieldFormatter();
    
        appendUpToUnique(newContentsRefactored, newDisplayContents, "N", names, 1, null, fieldFormatter, TERMINATOR);
    
        append(newContentsRefactored, newDisplayContents, "ORG", organization, fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContentsRefactored, newDisplayContents, "ADR", addresses, 1, null, fieldFormatter, TERMINATOR);
    
        List<Map<String,Set<String>>> phoneMetadata = buildPhoneMetadata(phones, phoneTypes);
        appendUpToUnique(newContentsRefactored, newDisplayContents, "TEL", phones, Integer.MAX_VALUE,
                         new VCardTelDisplayFormatter(phoneMetadata),
                         new VCardFieldFormatter(phoneMetadata), TERMINATOR);
    
        appendUpToUnique(newContentsRefactored, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContentsRefactored, newDisplayContents, "URL", urls, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        append(newContentsRefactored, newDisplayContents, "NOTE", note, fieldFormatter, TERMINATOR);
    
        newContentsRefactored.append("END:VCARD").append(TERMINATOR);
    
        return new String[] { newContentsRefactored.toString(), newDisplayContents.toString() };
      }
}
