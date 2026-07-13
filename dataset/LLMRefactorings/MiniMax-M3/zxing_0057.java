public class zxing_0057 {

      static String parseFieldsInGeneralPurpose(String rawInformation) throws NotFoundException {
        if (rawInformation.isEmpty()) {
          return null;
        }
    
        // Processing 2-digit AIs
    
        if (rawInformation.length() < 2) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        DataLength twoDigitDataLength = TWO_DIGIT_DATA_LENGTH.get(rawInformation.substring(0, 2));
        if (twoDigitDataLength != null) {
          return processAI(2, twoDigitDataLength, rawInformation);
        }
    
        if (rawInformation.length() < 3) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        String firstThreeDigits = rawInformation.substring(0, 3);
        DataLength threeDigitDataLength = THREE_DIGIT_DATA_LENGTH.get(firstThreeDigits);
        if (threeDigitDataLength != null) {
          return processAI(3, threeDigitDataLength, rawInformation);
        }
    
        if (rawInformation.length() < 4) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        DataLength threeDigitPlusDigitDataLength = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.get(firstThreeDigits);
        if (threeDigitPlusDigitDataLength != null) {
          return processAI(4, threeDigitPlusDigitDataLength, rawInformation);
        }
    
        DataLength firstFourDigitLength = FOUR_DIGIT_DATA_LENGTH.get(rawInformation.substring(0, 4));
        if (firstFourDigitLength != null) {
          return processAI(4, firstFourDigitLength, rawInformation);
        }
    
        throw NotFoundException.getNotFoundInstance();
      }

      private static String processAI(int aiSize, DataLength dataLength, String rawInformation) {
        if (dataLength.variable) {
          return processVariableAI(aiSize, dataLength.length, rawInformation);
        }
        return processFixedAI(aiSize, dataLength.length, rawInformation);
      }
}
