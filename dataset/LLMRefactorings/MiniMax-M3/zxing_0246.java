public class zxing_0246 {

      private static FormatInformation doDecodeFormatInformation(int maskedFormatInfo1, int maskedFormatInfo2) {
        // Find the int in FORMAT_INFO_DECODE_LOOKUP with fewest bits differing
        int bestDifference = Integer.MAX_VALUE;
        int bestFormatInfo = 0;
        for (int[] decodeInfo : FORMAT_INFO_DECODE_LOOKUP) {
          int targetInfo = decodeInfo[0];
          if (targetInfo == maskedFormatInfo1 || targetInfo == maskedFormatInfo2) {
            // Found an exact match
            return new FormatInformation(decodeInfo[1]);
          }
          int[] update = updateBestMatch(maskedFormatInfo1, targetInfo, decodeInfo[1], bestFormatInfo, bestDifference);
          bestFormatInfo = update[0];
          bestDifference = update[1];
          if (maskedFormatInfo1 != maskedFormatInfo2) {
            // also try the other option
            update = updateBestMatch(maskedFormatInfo2, targetInfo, decodeInfo[1], bestFormatInfo, bestDifference);
            bestFormatInfo = update[0];
            bestDifference = update[1];
          }
        }
        // Hamming distance of the 32 masked codes is 7, by construction, so <= 3 bits
        // differing means we found a match
        if (bestDifference <= 3) {
          return new FormatInformation(bestFormatInfo);
        }
        return null;
      }

      private static int[] updateBestMatch(int maskedFormatInfo, int targetInfo, int candidateFormatInfo, int currentBestFormatInfo, int currentBestDifference) {
        int bitsDifference = numBitsDiffering(maskedFormatInfo, targetInfo);
        if (bitsDifference < currentBestDifference) {
          return new int[]{candidateFormatInfo, bitsDifference};
        }
        return new int[]{currentBestFormatInfo, currentBestDifference};
      }
}
