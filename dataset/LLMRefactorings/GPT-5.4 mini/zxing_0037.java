public class zxing_0037 {

      @Override
      public AddressBookParsedResult parse(Result result) {
        String rawTextRefactored = getMassagedText(result);
        if (!rawTextRefactored.startsWith("MECARD:")) {
          return null;
        }
        String[] rawName = matchDoCoMoPrefixedField("N:", rawTextRefactored);
        if (rawName == null) {
          return null;
        }
        String name = parseName(rawName[0]);
        String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawTextRefactored, true);
        String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawTextRefactored);
        String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawTextRefactored);
        String note = matchSingleDoCoMoPrefixedField("NOTE:", rawTextRefactored, false);
        String[] addresses = matchDoCoMoPrefixedField("ADR:", rawTextRefactored);
        String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawTextRefactored, true);
        if (!isStringOfDigits(birthday, 8)) {
          // No reason to throw out the whole card because the birthday is formatted wrong.
          birthday = null;
        }
        String[] urls = matchDoCoMoPrefixedField("URL:", rawTextRefactored);
    
        // Although ORG may not be strictly legal in MECARD, it does exist in VCARD and we might as well
        // honor it when found in the wild.
        String org = matchSingleDoCoMoPrefixedField("ORG:", rawTextRefactored, true);
    
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
                                           org,
                                           birthday,
                                           null,
                                           urls,
                                           null);
      }
}
