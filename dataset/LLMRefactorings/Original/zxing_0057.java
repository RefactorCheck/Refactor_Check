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
          if (twoDigitDataLength.variable) {
            return processVariableAI(2, twoDigitDataLength.length, rawInformation);
          }
          return processFixedAI(2, twoDigitDataLength.length, rawInformation);
        }
    
        if (rawInformation.length() < 3) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        String firstThreeDigits = rawInformation.substring(0, 3);
        DataLength threeDigitDataLength = THREE_DIGIT_DATA_LENGTH.get(firstThreeDigits);
        if (threeDigitDataLength != null) {
          if (threeDigitDataLength.variable) {
            return processVariableAI(3, threeDigitDataLength.length, rawInformation);
          }
          return processFixedAI(3, threeDigitDataLength.length, rawInformation);
        }
    
        if (rawInformation.length() < 4) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        DataLength threeDigitPlusDigitDataLength = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.get(firstThreeDigits);
        if (threeDigitPlusDigitDataLength != null) {
          if (threeDigitPlusDigitDataLength.variable) {
            return processVariableAI(4, threeDigitPlusDigitDataLength.length, rawInformation);
          }
          return processFixedAI(4, threeDigitPlusDigitDataLength.length, rawInformation);
        }
    
        DataLength firstFourDigitLength = FOUR_DIGIT_DATA_LENGTH.get(rawInformation.substring(0, 4));
        if (firstFourDigitLength != null) {
          if (firstFourDigitLength.variable) {
            return processVariableAI(4, firstFourDigitLength.length, rawInformation);
          }
          return processFixedAI(4, firstFourDigitLength.length, rawInformation);
        }
    
        throw NotFoundException.getNotFoundInstance();
      }
}
