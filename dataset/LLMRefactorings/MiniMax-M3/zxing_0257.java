public class zxing_0257 {

    byte[] getEDFBytes() {
        int numberOfThirds = (int) Math.ceil(characterLength / 4.0);
        byte[] result = new byte[numberOfThirds * 3];
        int pos = fromPosition;
        int endPos = Math.min(fromPosition + characterLength - 1, input.length() - 1);
        for (int i = 0; i < numberOfThirds; i += 3) {
            int[] edfValues = collectEdfValues(pos, endPos);
            pos += 4;
            packTriplet(result, i, edfValues);
        }
        return result;
    }

    private int[] collectEdfValues(int startPos, int endPos) {
        int[] edfValues = new int[4];
        int pos = startPos;
        for (int j = 0; j < 4; j++) {
            if (pos <= endPos) {
                edfValues[j] = input.charAt(pos++) & 0x3f;
            } else {
                edfValues[j] = pos == endPos + 1 ? 0x1f : 0;
            }
        }
        return edfValues;
    }

    private void packTriplet(byte[] result, int index, int[] edfValues) {
        int val24 = edfValues[0] << 18;
        val24 |= edfValues[1] << 12;
        val24 |= edfValues[2] << 6;
        val24 |= edfValues[3];
        result[index] = (byte) ((val24 >> 16) & 0xff);
        result[index + 1] = (byte) ((val24 >> 8) & 0xff);
        result[index + 2] = (byte) (val24 & 0xff);
    }
}
