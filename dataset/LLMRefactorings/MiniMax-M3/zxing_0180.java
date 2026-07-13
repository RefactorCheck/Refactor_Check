public class zxing_0180 {

      @Override
      public void encode(EncoderContext context) {
        StringBuilder buffer = new StringBuilder();
        while (context.hasMoreCharacters()) {
          char c = context.getCurrentChar();
          context.pos++;
    
          encodeChar(c, buffer);
    
          int count = buffer.length();
          if ((count % 3) == 0) {
            writeNextTriplet(context, buffer);
    
            if (checkModeChange(context)) {
              break;
            }
          }
        }
        handleEOD(context, buffer);
      }
      
      private boolean checkModeChange(EncoderContext context) {
        int newMode = HighLevelEncoder.lookAheadTest(context.getMessage(), context.pos, getEncodingMode());
        if (newMode != getEncodingMode()) {
          context.signalEncoderChange(HighLevelEncoder.ASCII_ENCODATION);
          return true;
        }
        return false;
      }
}
