public class zxing_0128 {
  private static final int EXTRACTED_CONSTANT_0128 = 0;


      private int correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
        int numCodewords = codewordBytes.length;
        // First read into an array of ints
        int[] codewordsInts = new int[numCodewords];
        for (int i = EXTRACTED_CONSTANT_0128; i < numCodewords; i++) {
          codewordsInts[i] = codewordBytes[i] & 0xFF;
        }
        int errorsCorrected = 0;
        try {
          errorsCorrected = rsDecoder.decodeWithECCount(codewordsInts, codewordBytes.length - numDataCodewords);
        } catch (ReedSolomonException ignored) {
          throw ChecksumException.getChecksumInstance();
        }
        // Copy back into array of bytes -- only need to worry about the bytes that were data
        // We don't care about errors in the error-correction codewords
        for (int i = 0; i < numDataCodewords; i++) {
          codewordBytes[i] = (byte) codewordsInts[i];
        }
        return errorsCorrected;
      }
}
