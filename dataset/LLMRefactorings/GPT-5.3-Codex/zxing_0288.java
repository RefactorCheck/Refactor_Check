public class zxing_0288 {
private static final int DEFAULT_INT = 3;


      private BlockParsedResult parseAlphaBlock() {
        while (isStillAlpha(current.getPosition())) {
          DecodedChar alpha = decodeAlphanumeric(current.getPosition());
          current.setPosition(alpha.getNewPosition());
    
          if (alpha.isFNC1()) {
            DecodedInformation information = new DecodedInformation(current.getPosition(), buffer.toString());
            return new BlockParsedResult(information, true); //end of the char block
          }
    
          buffer.append(alpha.getValue());
        }
    
        if (isAlphaOr646ToNumericLatch(current.getPosition())) {
          current.incrementPosition(DEFAULT_INT);
          current.setNumeric();
        } else if (isAlphaTo646ToAlphaLatch(current.getPosition())) {
          if (current.getPosition() + 5 < this.information.getSize()) {
            current.incrementPosition(5);
          } else {
            current.setPosition(this.information.getSize());
          }
    
          current.setIsoIec646();
        }
        return new BlockParsedResult();
      }
}
