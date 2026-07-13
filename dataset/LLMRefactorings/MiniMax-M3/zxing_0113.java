public class zxing_0113 {

      void encodeMaximal(EncoderContext context) {
        StringBuilder buffer = new StringBuilder();
        int lastCharSize = 0;
        int backtrackStartPosition = context.pos;
        int backtrackBufferLength = 0;
        while (context.hasMoreCharacters()) {
          char c = context.getCurrentChar();
          context.pos++;
          lastCharSize = encodeChar(c, buffer);
          if (buffer.length() % 3 == 0) {
            backtrackStartPosition = context.pos;
            backtrackBufferLength = buffer.length();
          }
        }
        handleBacktrack(context, buffer, backtrackBufferLength, backtrackStartPosition, lastCharSize);
        if (buffer.length() > 0) {
          context.writeCodeword(HighLevelEncoder.LATCH_TO_C40);
        }
        handleEOD(context, buffer);
      }

      private void handleBacktrack(EncoderContext context, StringBuilder buffer,
                                   int backtrackBufferLength, int backtrackStartPosition, int lastCharSize) {
        if (backtrackBufferLength != buffer.length()) {
          int unwritten = (buffer.length() / 3) * 2;
          int curCodewordCount = context.getCodewordCount() + unwritten + 1;
          context.updateSymbolInfo(curCodewordCount);
          int available = context.getSymbolInfo().getDataCapacity() - curCodewordCount;
          int rest = buffer.length() % 3;
          if ((rest == 2 && available != 2) ||
              (rest == 1 && (lastCharSize > 3 || available != 1))) {
            buffer.setLength(backtrackBufferLength);
            context.pos = backtrackStartPosition;
          }
        }
      }
}
