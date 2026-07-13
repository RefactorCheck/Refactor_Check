public class zxing_0299 {

    private static final int FIRST_BLOCK_START = 0;
    private static final int FIRST_BLOCK_END = 10;
    private static final int FIRST_BLOCK_LENGTH = 10;
    private static final int SECOND_BLOCK_START = 20;
    private static final int MODE_234_END = 84;
    private static final int MODE_234_LENGTH = 40;
    private static final int MODE_234_DATAWORDS_LENGTH = 94;
    private static final int MODE_5_END = 68;
    private static final int MODE_5_LENGTH = 56;
    private static final int MODE_5_DATAWORDS_LENGTH = 78;
    private static final int MODE_MASK = 0x0F;

    public DecoderResult decode(BitMatrix bits,
                                Map<DecodeHintType,?> hints) throws FormatException, ChecksumException {
        BitMatrixParser parser = new BitMatrixParser(bits);
        byte[] codewords = parser.readCodewords();

        int errorsCorrected = correctErrors(codewords, FIRST_BLOCK_START, FIRST_BLOCK_END, FIRST_BLOCK_LENGTH, ALL);
        int mode = codewords[0] & MODE_MASK;
        byte[] datawords;
        switch (mode) {
            case 2:
            case 3:
            case 4:
                errorsCorrected += correctErrors(codewords, SECOND_BLOCK_START, MODE_234_END, MODE_234_LENGTH, EVEN);
                errorsCorrected += correctErrors(codewords, SECOND_BLOCK_START, MODE_234_END, MODE_234_LENGTH, ODD);
                datawords = new byte[MODE_234_DATAWORDS_LENGTH];
                break;
            case 5:
                errorsCorrected += correctErrors(codewords, SECOND_BLOCK_START, MODE_5_END, MODE_5_LENGTH, EVEN);
                errorsCorrected += correctErrors(codewords, SECOND_BLOCK_START, MODE_5_END, MODE_5_LENGTH, ODD);
                datawords = new byte[MODE_5_DATAWORDS_LENGTH];
                break;
            default:
                throw FormatException.getFormatInstance();
        }

        System.arraycopy(codewords, FIRST_BLOCK_START, datawords, FIRST_BLOCK_START, FIRST_BLOCK_LENGTH);
        System.arraycopy(codewords, SECOND_BLOCK_START, datawords, FIRST_BLOCK_LENGTH, datawords.length - FIRST_BLOCK_LENGTH);

        DecoderResult result = DecodedBitStreamParser.decode(datawords, mode);
        result.setErrorsCorrected(errorsCorrected);
        return result;
    }
}
