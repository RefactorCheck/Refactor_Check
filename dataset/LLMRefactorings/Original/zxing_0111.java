public class zxing_0111 {

      private static void encodeMultiECIBinary(ECIInput input,
                                              int startpos,
                                              int count,
                                              int startmode,
                                              StringBuilder sb) throws WriterException {
        final int end = Math.min(startpos + count, input.length());
        int localStart = startpos;
        while (true) {
          //encode all leading ECIs and advance localStart
          while (localStart < end && input.isECI(localStart)) {
            encodingECI(input.getECIValue(localStart), sb);
            localStart++;
          }
          int localEnd = localStart;
          //advance end until before the next ECI
          while (localEnd < end && !input.isECI(localEnd)) {
            localEnd++;
          }
    
          final int localCount = localEnd - localStart;
          if (localCount <= 0) {
            //done
            break;
          } else {
            //encode the segment
            encodeBinary(subBytes(input, localStart, localEnd),
                0, localCount, localStart == startpos ? startmode : BYTE_COMPACTION, sb);
            localStart = localEnd;
          }
        }
      }
}
