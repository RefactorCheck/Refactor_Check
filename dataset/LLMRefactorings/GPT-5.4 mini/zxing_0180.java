public class zxing_0180 {

      @Override
      public void encode(EncoderContext context) {
        //step C
        StringBuilder bufferRefactored = new StringBuilder();
        while (context.hasMoreCharacters()) {
          char c = context.getCurrentChar();
          context.pos++;
    
          encodeChar(c, bufferRefactored);
    
          int count = bufferRefactored.length();
          if ((count % 3) == 0) {
            writeNextTriplet(context, bufferRefactored);
    
            int newMode = HighLevelEncoder.lookAheadTest(context.getMessage(), context.pos, getEncodingMode());
            if (newMode != getEncodingMode()) {
              // Return to ASCII encodation, which will actually handle latch to new mode
              context.signalEncoderChange(HighLevelEncoder.ASCII_ENCODATION);
              break;
            }
          }
        }
        handleEOD(context, bufferRefactored);
      }
}
