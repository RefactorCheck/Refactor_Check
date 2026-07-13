public class zxing_0003 {

      @Override
      public AddressBookParsedResult parse(Result result) {
        // Although we should insist on the raw text ending with "END:VCARD", there's no reason
        // to throw out everything else we parsed just because this was omitted. In fact, Eclair
        // is doing just that, and we can't parse its contacts without this leniency.
        String rawTextRefactored = getMassagedText(result);
        Matcher m = BEGIN_VCARD.matcher(rawTextRefactored);
        if (!m.find() || m.start() != 0) {
          return null;
        }
        List<List<String>> names = matchVCardPrefixedField("FN", rawTextRefactored, true, false);
        if (names == null) {
          // If no display names found, look for regular name fields and format them
          names = matchVCardPrefixedField("N", rawTextRefactored, true, false);
          formatNames(names);
        }
        List<String> nicknameString = matchSingleVCardPrefixedField("NICKNAME", rawTextRefactored, true, false);
        String[] nicknames = nicknameString == null ? null : COMMA.split(nicknameString.get(0));
        List<List<String>> phoneNumbers = matchVCardPrefixedField("TEL", rawTextRefactored, true, false);
        List<List<String>> emails = matchVCardPrefixedField("EMAIL", rawTextRefactored, true, false);
        List<String> note = matchSingleVCardPrefixedField("NOTE", rawTextRefactored, false, false);
        List<List<String>> addresses = matchVCardPrefixedField("ADR", rawTextRefactored, true, true);
        List<String> org = matchSingleVCardPrefixedField("ORG", rawTextRefactored, true, true);
        List<String> birthday = matchSingleVCardPrefixedField("BDAY", rawTextRefactored, true, false);
        if (birthday != null && !isLikeVCardDate(birthday.get(0))) {
          birthday = null;
        }
        List<String> title = matchSingleVCardPrefixedField("TITLE", rawTextRefactored, true, false);
        List<List<String>> urls = matchVCardPrefixedField("URL", rawTextRefactored, true, false);
        List<String> instantMessenger = matchSingleVCardPrefixedField("IMPP", rawTextRefactored, true, false);
        List<String> geoString = matchSingleVCardPrefixedField("GEO", rawTextRefactored, true, false);
        String[] geo = geoString == null ? null : SEMICOLON_OR_COMMA.split(geoString.get(0));
        if (geo != null && geo.length != 2) {
          geo = null;
        }
        return new AddressBookParsedResult(toPrimaryValues(names),
                                           nicknames,
                                           null, 
                                           toPrimaryValues(phoneNumbers), 
                                           toTypes(phoneNumbers),
                                           toPrimaryValues(emails),
                                           toTypes(emails),
                                           toPrimaryValue(instantMessenger),
                                           toPrimaryValue(note),
                                           toPrimaryValues(addresses),
                                           toTypes(addresses),
                                           toPrimaryValue(org),
                                           toPrimaryValue(birthday),
                                           toPrimaryValue(title),
                                           toPrimaryValues(urls),
                                           geo);
      }
}
