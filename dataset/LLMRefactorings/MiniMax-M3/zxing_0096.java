public class zxing_0096 {

    private static final int UNLATCH_CODEWORD = 254;
    private static final int MAX_DIGIT_VALUE = 14;
    private static final int MAX_LETTER_VALUE = 40;
    private static final int DIGIT_OFFSET = 44;
    private static final int LETTER_OFFSET = 51;

    private static void decodeAnsiX12Segment(BitSource bits,
                                             ECIStringBuilder result) throws FormatException {
        // Three ANSI X12 values are encoded in a 16-bit value as
        // (1600 * C1) + (40 * C2) + C3 + 1

        int[] cValues = new int[3];
        do {
            // If there is only one byte left then it will be encoded as ASCII
            if (bits.available() == 8) {
                return;
            }
            int firstByte = bits.readBits(8);
            if (firstByte == UNLATCH_CODEWORD) {  // Unlatch codeword
                return;
            }

            parseTwoBytes(firstByte, bits.readBits(8), cValues);

            for (int i = 0; i < 3; i++) {
                int cValue = cValues[i];
                switch (cValue) {
                    case 0: // X12 segment terminator <CR>
                        result.append('\r');
                        break;
                    case 1: // X12 segment separator *
                        result.append('*');
                        break;
                    case 2: // X12 sub-element separator >
                        result.append('>');
                        break;
                    case 3: // space
                        result.append(' ');
                        break;
                    default:
                        if (cValue < MAX_DIGIT_VALUE) {  // 0 - 9
                            result.append((char) (cValue + DIGIT_OFFSET));
                        } else if (cValue < MAX_LETTER_VALUE) {  // A - Z
                            result.append((char) (cValue + LETTER_OFFSET));
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                }
            }
        } while (bits.available() > 0);
    }
}
