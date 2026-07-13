public class zxing_0170 {

    byte[] readCodewords() throws FormatException {
        FormatInformation formatInfo = readFormatInformation();
        Version version = readVersion();

        DataMask dataMask = DataMask.values()[formatInfo.getDataMask()];
        int dimension = bitMatrix.getHeight();
        dataMask.unmaskBitMatrix(bitMatrix, dimension);

        BitMatrix functionPattern = version.buildFunctionPattern();

        boolean readingUp = true;
        byte[] result = new byte[version.getTotalCodewords()];
        int resultOffset = 0;
        int currentByte = 0;
        int bitsRead = 0;
        for (int j = dimension - 1; j > 0; j -= 2) {
            if (j == 6) {
                j--;
            }
            for (int count = 0; count < dimension; count++) {
                int i = readingUp ? dimension - 1 - count : count;
                for (int col = 0; col < 2; col++) {
                    if (!functionPattern.get(j - col, i)) {
                        currentByte = readBit(j - col, i, currentByte);
                        bitsRead++;
                        if (bitsRead == 8) {
                            result[resultOffset++] = (byte) currentByte;
                            bitsRead = 0;
                            currentByte = 0;
                        }
                    }
                }
            }
            readingUp ^= true;
        }
        if (resultOffset != version.getTotalCodewords()) {
            throw FormatException.getFormatInstance();
        }
        return result;
    }

    private int readBit(int x, int y, int currentByte) {
        currentByte <<= 1;
        if (bitMatrix.get(x, y)) {
            currentByte |= 1;
        }
        return currentByte;
    }
}
