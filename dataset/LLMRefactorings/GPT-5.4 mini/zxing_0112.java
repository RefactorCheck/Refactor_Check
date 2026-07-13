public class zxing_0112 {

      private static BlockParsedResult parseNumericBlock() throws FormatException {
        while (isStillNumeric(current.getPosition())) {
          DecodedNumeric numeric = decodeNumeric(current.getPosition());
          current.setPosition(numeric.getNewPosition());
    
          if (numeric.isFirstDigitFNC1()) {
            DecodedInformation information;
            if (numeric.isSecondDigitFNC1()) {
              information = new DecodedInformation(current.getPosition(), buffer.toString());
            } else {
              information = new DecodedInformation(current.getPosition(), buffer.toString(), numeric.getSecondDigit());
            }
            return new BlockParsedResult(information, true);
          }
          buffer.append(numeric.getFirstDigit());
    
          if (numeric.isSecondDigitFNC1()) {
            DecodedInformation information = new DecodedInformation(current.getPosition(), buffer.toString());
            return new BlockParsedResult(information, true);
          }
          buffer.append(numeric.getSecondDigit());
        }
    
        if (isNumericToAlphaNumericLatch(current.getPosition())) {
          current.setAlpha();
          current.incrementPosition(4);
        }
        return new BlockParsedResult();
      }
}
