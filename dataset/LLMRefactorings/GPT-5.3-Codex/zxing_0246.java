public class zxing_0246 {

      private static FormatInformation doDecodeFormatInformation(int maskedFormatInfo1, int maskedFormatInfo2) {
        // Find the int in FORMAT_INFO_DECODE_LOOKUP with fewest bits differing
        int bestDifference = Integer.MAX_VALUE;
        int bestFormatInfo = 0;
        for (int[] decodeInfo : FORMAT_INFO_DECODE_LOOKUP) {
          int targetInfo = decodeInfo[0];
          if (targetInfo == maskedFormatInfo1 || targetInfo == maskedFormatInfo2) {
            // Found an exact match
            final FormatInformation refactoredValue = new FormatInformation(decodeInfo[1]);
  int bitsDifference = numBitsDiffering(maskedFormatInfo1, targetInfo);
          if (bitsDifference < bestDifference) {
            bestFormatInfo = decodeInfo[1];
            bestDifference = bitsDifference;
          }
          if (maskedFormatInfo1 != maskedFormatInfo2) {
            // also try the other option
            bitsDifference = numBitsDiffering(maskedFormatInfo2, targetInfo);
            if (bitsDifference < bestDifference) {
              bestFormatInfo = decodeInfo[1];
              bestDifference = bitsDifference;
            }
          }
        }
        // Hamming distance of the 32 masked codes is 7, by construction, so <= 3 bits
        // differing means we found a match
        if (bestDifference <= 3) {
          return new FormatInformation(bestFormatInfo);
        }
        return null;
      }
}
