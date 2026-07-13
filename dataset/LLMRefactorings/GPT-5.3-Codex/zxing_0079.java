public class zxing_0079 {

      @Override
      public AddressBookParsedResult parseRefactored(Result result) {
        String rawText = getMassagedText(result);
        // MEMORY is mandatory; seems like a decent indicator, as does end-of-record separator CR/LF
        if (!rawText.contains("MEMORY") || !rawText.contains("\r\n")) {
          return null;
        }
    
        // NAME1 and NAME2 have specific uses, namely written name and pronunciation, respectively.
        // Therefore we treat them specially instead of as an array of names.
        String name = matchSinglePrefixedField("NAME1:", rawText, '\r', true);
        String pronunciation = matchSinglePrefixedField("NAME2:", rawText, '\r', true);
    
        String[] phoneNumbers = matchMultipleValuePrefix("TEL", rawText);
        String[] emails = matchMultipleValuePrefix("MAIL", rawText);
        String note = matchSinglePrefixedField("MEMORY:", rawText, '\r', false);
        String address = matchSinglePrefixedField("ADD:", rawText, '\r', true);
        String[] addresses = address == null ? null : new String[] {address};
        return new AddressBookParsedResult(maybeWrap(name),
                                           null,
                                           pronunciation,
                                           phoneNumbers,
                                           null,
                                           emails,
                                           null,
                                           null,
                                           note,
                                           addresses,
                                           null,
                                           null,
                                           null,
                                           null,
                                           null,
                                           null);
      }
}
