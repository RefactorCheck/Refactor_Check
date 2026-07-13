public class zxing_0193 {

      private static final int AZTEC_COMPACT_NUM_CODEWORDS = 7;
      private static final int AZTEC_COMPACT_NUM_DATA_CODEWORDS = 2;
      private static final int AZTEC_NUM_CODEWORDS = 10;
      private static final int AZTEC_NUM_DATA_CODEWORDS = 4;

      private static CorrectedParameter getCorrectedParameterData(long parameterData,
                                                                  boolean compact) throws NotFoundException {
        int numCodewords;
        int numDataCodewords;
    
        if (compact) {
          numCodewords = AZTEC_COMPACT_NUM_CODEWORDS;
          numDataCodewords = AZTEC_COMPACT_NUM_DATA_CODEWORDS;
        } else {
          numCodewords = AZTEC_NUM_CODEWORDS;
          numDataCodewords = AZTEC_NUM_DATA_CODEWORDS;
        }
    
        int numECCodewords = numCodewords - numDataCodewords;
        int[] parameterWords = new int[numCodewords];
        for (int i = numCodewords - 1; i >= 0; --i) {
          parameterWords[i] = (int) parameterData & 0xF;
          parameterData >>= 4;
        }
    
        int errorsCorrected = 0;
        try {
          ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(GenericGF.AZTEC_PARAM);
          errorsCorrected = rsDecoder.decodeWithECCount(parameterWords, numECCodewords);
        } catch (ReedSolomonException ignored) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        int result = 0;
        for (int i = 0; i < numDataCodewords; i++) {
          result = (result << 4) + parameterWords[i];
        }
        return new CorrectedParameter(result, errorsCorrected);
      }
}
