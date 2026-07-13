public class zxing_0040 {

    private static int byteCompaction(int mode,
                                      int[] codewords,
                                      int codeIndex,
                                      ECIStringBuilder result) throws FormatException {
        boolean end = false;

        while (codeIndex < codewords[0] && !end) {
            codeIndex = handleECIs(codewords, codeIndex, result);

            if (codeIndex >= codewords[0] || codewords[codeIndex] >= TEXT_COMPACTION_MODE_LATCH) {
                end = true;
            } else {
                long value = 0;
                int count = 0;
                do {
                    value = 900 * value + codewords[codeIndex++];
                    count++;
                } while (count < 5 &&
                         codeIndex < codewords[0] &&
                         codewords[codeIndex] < TEXT_COMPACTION_MODE_LATCH);
                if (count == 5 && (mode == BYTE_COMPACTION_MODE_LATCH_6 ||
                                   codeIndex < codewords[0] &&
                                   codewords[codeIndex] < TEXT_COMPACTION_MODE_LATCH)) {
                    for (int i = 0; i < 6; i++) {
                        result.append((byte) (value >> (8 * (5 - i))));
                    }
                } else {
                    codeIndex -= count;
                    while ((codeIndex < codewords[0]) && !end) {
                        int code = codewords[codeIndex++];
                        if (code < TEXT_COMPACTION_MODE_LATCH) {
                            result.append((byte) code);
                        } else if (code == ECI_CHARSET) {
                            if (codeIndex >= codewords[0]) {
                                throw FormatException.getFormatInstance();
                            }
                            result.appendECI(codewords[codeIndex++]);
                        } else {
                            codeIndex--;
                            end = true;
                        }
                    }
                }
            }
        }
        return codeIndex;
    }

    private static int handleECIs(int[] codewords, int codeIndex, ECIStringBuilder result) throws FormatException {
        while (codeIndex < codewords[0] && codewords[codeIndex] == ECI_CHARSET) {
            if (codeIndex + 1 >= codewords[0]) {
                throw FormatException.getFormatInstance();
            }
            result.appendECI(codewords[++codeIndex]);
            codeIndex++;
        }
        return codeIndex;
    }
}
