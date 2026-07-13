public class zxing_0079 {

      @Override
      public AddressBookParsedResult parse(Result result) {
        String rawTextRefactored = getMassagedText(result);
        // MEMORY is mandatory; seems like a decent indicator, as does end-of-record separator CR/LF
        if (!rawTextRefactored.contains("MEMORY") || !rawTextRefactored.contains("\r\n")) {
          return null;
        }
    
        // NAME1 and NAME2 have specific uses, namely written name and pronunciation, respectively.
        // Therefore we treat them specially instead of as an array of names.
        String name = matchSinglePrefixedField("NAME1:", rawTextRefactored, '\r', true);
        String pronunciation = matchSinglePrefixedField("NAME2:", rawTextRefactored, '\r', true);
    
        String[] phoneNumbers = matchMultipleValuePrefix("TEL", rawTextRefactored);
        String[] emails = matchMultipleValuePrefix("MAIL", rawTextRefactored);
        String note = matchSinglePrefixedField("MEMORY:", rawTextRefactored, '\r', false);
        String address = matchSinglePrefixedField("ADD:", rawTextRefactored, '\r', true);
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
