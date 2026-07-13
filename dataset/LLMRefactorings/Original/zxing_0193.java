public class zxing_0193 {

      private static CorrectedParameter getCorrectedParameterData(long parameterData,
                                                                  boolean compact) throws NotFoundException {
        int numCodewords;
        int numDataCodewords;
    
        if (compact) {
          numCodewords = 7;
          numDataCodewords = 2;
        } else {
          numCodewords = 10;
          numDataCodewords = 4;
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
    
        // Toss the error correction.  Just return the data as an integer
        int result = 0;
        for (int i = 0; i < numDataCodewords; i++) {
          result = (result << 4) + parameterWords[i];
        }
        return new CorrectedParameter(result, errorsCorrected);
      }
}
