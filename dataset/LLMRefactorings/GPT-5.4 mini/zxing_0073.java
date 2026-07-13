public class zxing_0073 {

      @Override
      public String parseInformation() throws NotFoundException, FormatException {
        if (this.getInformation().getSize() < HEADER_SIZE + GTIN_SIZE) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        StringBuilder bufRefactored = new StringBuilder();
    
        encodeCompressedGtin(bufRefactored, HEADER_SIZE);
    
        int lastAIdigit =
            this.getGeneralDecoder().extractNumericValueFromBitArray(HEADER_SIZE + GTIN_SIZE, LAST_DIGIT_SIZE);
        bufRefactored.append("(392");
        bufRefactored.append(lastAIdigit);
        bufRefactored.append(')');
    
        DecodedInformation decodedInformation =
            this.getGeneralDecoder().decodeGeneralPurposeField(HEADER_SIZE + GTIN_SIZE + LAST_DIGIT_SIZE, null);
        bufRefactored.append(decodedInformation.getNewString());
    
        return bufRefactored.toString();
      }
}
