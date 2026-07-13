public class zxing_0079 {

    private static final String MEMORY_INDICATOR = "MEMORY";
    private static final String RECORD_SEPARATOR = "\r\n";
    private static final String NAME1_PREFIX = "NAME1:";
    private static final String NAME2_PREFIX = "NAME2:";
    private static final String TEL_PREFIX = "TEL";
    private static final String MAIL_PREFIX = "MAIL";
    private static final String MEMORY_PREFIX = "MEMORY:";
    private static final String ADD_PREFIX = "ADD:";

    @Override
    public AddressBookParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!rawText.contains(MEMORY_INDICATOR) || !rawText.contains(RECORD_SEPARATOR)) {
            return null;
        }

        String name = matchSinglePrefixedField(NAME1_PREFIX, rawText, '\r', true);
        String pronunciation = matchSinglePrefixedField(NAME2_PREFIX, rawText, '\r', true);

        String[] phoneNumbers = matchMultipleValuePrefix(TEL_PREFIX, rawText);
        String[] emails = matchMultipleValuePrefix(MAIL_PREFIX, rawText);
        String note = matchSinglePrefixedField(MEMORY_PREFIX, rawText, '\r', false);
        String address = matchSinglePrefixedField(ADD_PREFIX, rawText, '\r', true);
        String[] addresses = address == null ? null : new String[]{address};
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
