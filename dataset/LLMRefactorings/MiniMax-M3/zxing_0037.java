public class zxing_0037 {

      @Override
      public AddressBookParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!rawText.startsWith("MECARD:")) {
          return null;
        }
        String[] rawName = matchDoCoMoPrefixedField("N:", rawText);
        if (rawName == null) {
          return null;
        }
        String name = parseName(rawName[0]);
        String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawText, true);
        String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawText);
        String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawText);
        String note = matchSingleDoCoMoPrefixedField("NOTE:", rawText, false);
        String[] addresses = matchDoCoMoPrefixedField("ADR:", rawText);
        String birthday = getValidatedBirthday(rawText);
        String[] urls = matchDoCoMoPrefixedField("URL:", rawText);
    
        // Although ORG may not be strictly legal in MECARD, it does exist in VCARD and we might as well
        // honor it when found in the wild.
        String org = matchSingleDoCoMoPrefixedField("ORG:", rawText, true);
    
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

      private String getValidatedBirthday(String rawText) {
        String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawText, true);
        if (!isStringOfDigits(birthday, 8)) {
          // No reason to throw out the whole card because the birthday is formatted wrong.
          return null;
        }
        return birthday;
      }
}
