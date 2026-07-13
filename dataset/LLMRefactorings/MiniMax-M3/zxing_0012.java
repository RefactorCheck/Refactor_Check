public class zxing_0012 {

    private static final String PREFIX_310 = "310";
    private static final String PREFIX_320 = "320";
    private static final String SUFFIX_11 = "11";
    private static final String SUFFIX_13 = "13";
    private static final String SUFFIX_15 = "15";
    private static final String SUFFIX_17 = "17";

    public static AbstractExpandedDecoder createDecoder(BitArray information) {
        if (information.get(1)) {
            return new AI01AndOtherAIs(information);
        }
        if (!information.get(2)) {
            return new AnyAIDecoder(information);
        }

        int fourBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 4);

        switch (fourBitEncodationMethod) {
            case 4: return new AI013103decoder(information);
            case 5: return new AI01320xDecoder(information);
        }

        int fiveBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 5);
        switch (fiveBitEncodationMethod) {
            case 12: return new AI01392xDecoder(information);
            case 13: return new AI01393xDecoder(information);
        }

        int sevenBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 7);
        switch (sevenBitEncodationMethod) {
            case 56: return new AI013x0x1xDecoder(information, PREFIX_310, SUFFIX_11);
            case 57: return new AI013x0x1xDecoder(information, PREFIX_320, SUFFIX_11);
            case 58: return new AI013x0x1xDecoder(information, PREFIX_310, SUFFIX_13);
            case 59: return new AI013x0x1xDecoder(information, PREFIX_320, SUFFIX_13);
            case 60: return new AI013x0x1xDecoder(information, PREFIX_310, SUFFIX_15);
            case 61: return new AI013x0x1xDecoder(information, PREFIX_320, SUFFIX_15);
            case 62: return new AI013x0x1xDecoder(information, PREFIX_310, SUFFIX_17);
            case 63: return new AI013x0x1xDecoder(information, PREFIX_320, SUFFIX_17);
        }

        throw new IllegalStateException("unknown decoder: " + information);
    }
}
