public class zxing_0112 {

      private BlockParsedResult parseNumericBlock() throws FormatException {
        while (isStillNumeric(current.getPosition())) {
          DecodedNumeric numeric = decodeNumeric(current.getPosition());
          current.setPosition(numeric.getNewPosition());
    
          if (numeric.isFirstDigitFNC1()) {
            return createFNC1Result(numeric.isSecondDigitFNC1() ? -1 : numeric.getSecondDigit());
          }
          buffer.append(numeric.getFirstDigit());
    
          if (numeric.isSecondDigitFNC1()) {
            return createFNC1Result(-1);
          }
          buffer.append(numeric.getSecondDigit());
        }
    
        if (isNumericToAlphaNumericLatch(current.getPosition())) {
          current.setAlpha();
          current.incrementPosition(4);
        }
        return new BlockParsedResult();
      }
    
      private BlockParsedResult createFNC1Result(int secondDigit) {
        DecodedInformation information;
        if (secondDigit == -1) {
          information = new DecodedInformation(current.getPosition(), buffer.toString());
        } else {
          information = new DecodedInformation(current.getPosition(), buffer.toString(), secondDigit);
        }
        return new BlockParsedResult(information, true);
      }
}
