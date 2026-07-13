public class zxing_0111 {

      private static void encodeMultiECIBinary(ECIInput input,
                                              int startpos,
                                              int count,
                                              int startmode,
                                              StringBuilder sb) throws WriterException {
        final int end = Math.min(startpos + count, input.length());
        int localStart = startpos;
        while (true) {
          localStart = encodeLeadingECIs(input, localStart, end, sb);
          int localEnd = localStart;
          while (localEnd < end && !input.isECI(localEnd)) {
            localEnd++;
          }
    
          final int localCount = localEnd - localStart;
          if (localCount <= 0) {
            break;
          } else {
            encodeBinary(subBytes(input, localStart, localEnd),
                0, localCount, localStart == startpos ? startmode : BYTE_COMPACTION, sb);
            localStart = localEnd;
          }
        }
      }

      private static int encodeLeadingECIs(ECIInput input, int start, int end, StringBuilder sb) throws WriterException {
        int pos = start;
        while (pos < end && input.isECI(pos)) {
          encodingECI(input.getECIValue(pos), sb);
          pos++;
        }
        return pos;
      }
}
