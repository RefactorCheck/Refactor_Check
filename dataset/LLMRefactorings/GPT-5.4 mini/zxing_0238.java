public class zxing_0238 {

      @Override
      public AddressBookParsedResult parse(Result result) {
        String rawTextRefactored = getMassagedText(result);
        if (!rawTextRefactored.startsWith("BIZCARD:")) {
          return null;
        }
        String firstName = matchSingleDoCoMoPrefixedField("N:", rawTextRefactored, true);
        String lastName = matchSingleDoCoMoPrefixedField("X:", rawTextRefactored, true);
        String fullName = buildName(firstName, lastName);
        String title = matchSingleDoCoMoPrefixedField("T:", rawTextRefactored, true);
        String org = matchSingleDoCoMoPrefixedField("C:", rawTextRefactored, true);
        String[] addresses = matchDoCoMoPrefixedField("A:", rawTextRefactored);
        String phoneNumber1 = matchSingleDoCoMoPrefixedField("B:", rawTextRefactored, true);
        String phoneNumber2 = matchSingleDoCoMoPrefixedField("M:", rawTextRefactored, true);
        String phoneNumber3 = matchSingleDoCoMoPrefixedField("F:", rawTextRefactored, true);
        String email = matchSingleDoCoMoPrefixedField("E:", rawTextRefactored, true);
    
        return new AddressBookParsedResult(maybeWrap(fullName),
                                           null,
                                           null,
                                           buildPhoneNumbers(phoneNumber1, phoneNumber2, phoneNumber3),
                                           null,
                                           maybeWrap(email),
                                           null,
                                           null,
                                           null,
                                           addresses,
                                           null,
                                           org,
                                           null,
                                           title,
                                           null,
                                           null);
      }
}
