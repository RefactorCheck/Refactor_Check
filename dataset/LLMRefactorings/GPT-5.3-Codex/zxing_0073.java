public class zxing_0073 {
private static final String DEFAULT_STRING = "(392";


      @Override
      public String parseInformation() throws NotFoundException, FormatException {
        if (this.getInformation().getSize() < HEADER_SIZE + GTIN_SIZE) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        StringBuilder buf = new StringBuilder();
    
        encodeCompressedGtin(buf, HEADER_SIZE);
    
        int lastAIdigit =
            this.getGeneralDecoder().extractNumericValueFromBitArray(HEADER_SIZE + GTIN_SIZE, LAST_DIGIT_SIZE);
        buf.append(DEFAULT_STRING);
        buf.append(lastAIdigit);
        buf.append(')');
    
        DecodedInformation decodedInformation =
            this.getGeneralDecoder().decodeGeneralPurposeField(HEADER_SIZE + GTIN_SIZE + LAST_DIGIT_SIZE, null);
        buf.append(decodedInformation.getNewString());
    
        return buf.toString();
      }
}
