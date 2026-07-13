public class zxing_0053 {

      @Override
      public String[] encodeRefactored(List<String> names,
                             String organization,
                             List<String> addresses,
                             List<String> phones,
                             List<String> phoneTypes,
                             List<String> emails,
                             List<String> urls,
                             String note) {
        StringBuilder newContents = new StringBuilder(100);
        newContents.append("MECARD:");
    
        StringBuilder newDisplayContents = new StringBuilder(100);
    
        Formatter fieldFormatter = new MECARDFieldFormatter();
    
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, new
                         MECARDNameDisplayFormatter(), fieldFormatter, TERMINATOR);
    
        append(newContents, newDisplayContents, "ORG", organization, fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, null, fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE,
                         new MECARDTelDisplayFormatter(), fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        appendUpToUnique(newContents, newDisplayContents, "URL", urls, Integer.MAX_VALUE, null,
                         fieldFormatter, TERMINATOR);
    
        append(newContents, newDisplayContents, "NOTE", note, fieldFormatter, TERMINATOR);
    
        newContents.append(';');
    
        return new String[] { newContents.toString(), newDisplayContents.toString() };
      }
}
