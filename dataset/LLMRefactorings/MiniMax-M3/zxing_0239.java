public class zxing_0239 {

      private int correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
        int[] codewordsInts = readCodewordsAsInts(codewordBytes);
        int errorsCorrected;
        try {
          errorsCorrected = rsDecoder.decodeWithECCount(codewordsInts, codewordBytes.length - numDataCodewords);
        } catch (ReedSolomonException ignored) {
          throw ChecksumException.getChecksumInstance();
        }
        copyCorrectedBytes(codewordBytes, codewordsInts, numDataCodewords);
        return errorsCorrected;
      }

      private int[] readCodewordsAsInts(byte[] codewordBytes) {
        int numCodewords = codewordBytes.length;
        int[] codewordsInts = new int[numCodewords];
        for (int i = 0; i < numCodewords; i++) {
          codewordsInts[i] = codewordBytes[i] & 0xFF;
        }
        return codewordsInts;
      }

      private void copyCorrectedBytes(byte[] codewordBytes, int[] codewordsInts, int numDataCodewords) {
        for (int i = 0; i < numDataCodewords; i++) {
          codewordBytes[i] = (byte) codewordsInts[i];
        }
      }
}
