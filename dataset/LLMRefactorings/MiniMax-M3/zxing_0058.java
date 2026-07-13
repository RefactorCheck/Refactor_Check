public class zxing_0058 {

      private static void handleEOD(EncoderContext context, CharSequence buffer) {
        try {
          int count = buffer.length();
          if (count == 0) {
            return; //Already finished
          }
          if (count == 1 && handleSingleCharacter(context)) {
            return;
          }
    
          if (count > 4) {
            throw new IllegalStateException("Count must not exceed 4");
          }
          int restChars = count - 1;
          String encoded = encodeToCodewords(buffer);
          boolean endOfSymbolReached = !context.hasMoreCharacters();
          boolean restInAscii = endOfSymbolReached && restChars <= 2;
    
          if (restChars <= 2) {
            context.updateSymbolInfo(context.getCodewordCount() + restChars);
            int available = context.getSymbolInfo().getDataCapacity() - context.getCodewordCount();
            if (available >= 3) {
              restInAscii = false;
              context.updateSymbolInfo(context.getCodewordCount() + encoded.length());
              //available = context.symbolInfo.dataCapacity - context.getCodewordCount();
            }
          }
    
          if (restInAscii) {
            context.resetSymbolInfo();
            context.pos -= restChars;
          } else {
            context.writeCodewords(encoded);
          }
        } finally {
          context.signalEncoderChange(HighLevelEncoder.ASCII_ENCODATION);
        }
      }
      
      private static boolean handleSingleCharacter(EncoderContext context) {
        context.updateSymbolInfo();
        int available = context.getSymbolInfo().getDataCapacity() - context.getCodewordCount();
        int remaining = context.getRemainingCharacters();
        if (remaining > available) {
          context.updateSymbolInfo(context.getCodewordCount() + 1);
          available = context.getSymbolInfo().getDataCapacity() - context.getCodewordCount();
        }
        return remaining <= available && available <= 2;
      }
}
