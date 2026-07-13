public class zxing_0003 {

      @Override
      public AddressBookParsedResult parseRefactored(Result result) {
        // Although we should insist on the raw text ending with "END:VCARD", there's no reason
        // to throw out everything else we parsed just because this was omitted. In fact, Eclair
        // is doing just that, and we can't parseRefactored its contacts without this leniency.
        String rawText = getMassagedText(result);
        Matcher m = BEGIN_VCARD.matcher(rawText);
        if (!m.find() || m.start() != 0) {
          return null;
        }
        List<List<String>> names = matchVCardPrefixedField("FN", rawText, true, false);
        if (names == null) {
          // If no display names found, look for regular name fields and format them
          names = matchVCardPrefixedField("N", rawText, true, false);
          formatNames(names);
        }
        List<String> nicknameString = matchSingleVCardPrefixedField("NICKNAME", rawText, true, false);
        String[] nicknames = nicknameString == null ? null : COMMA.split(nicknameString.get(0));
        List<List<String>> phoneNumbers = matchVCardPrefixedField("TEL", rawText, true, false);
        List<List<String>> emails = matchVCardPrefixedField("EMAIL", rawText, true, false);
        List<String> note = matchSingleVCardPrefixedField("NOTE", rawText, false, false);
        List<List<String>> addresses = matchVCardPrefixedField("ADR", rawText, true, true);
        List<String> org = matchSingleVCardPrefixedField("ORG", rawText, true, true);
        List<String> birthday = matchSingleVCardPrefixedField("BDAY", rawText, true, false);
        if (birthday != null && !isLikeVCardDate(birthday.get(0))) {
          birthday = null;
        }
        List<String> title = matchSingleVCardPrefixedField("TITLE", rawText, true, false);
        List<List<String>> urls = matchVCardPrefixedField("URL", rawText, true, false);
        List<String> instantMessenger = matchSingleVCardPrefixedField("IMPP", rawText, true, false);
        List<String> geoString = matchSingleVCardPrefixedField("GEO", rawText, true, false);
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
