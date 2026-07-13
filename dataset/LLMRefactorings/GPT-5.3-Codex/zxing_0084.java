public class zxing_0084 {

      private static int estimateBlackPoint(int[] buckets, boolean normalize) throws NotFoundException {
        // Find the tallest peak in the histogram.
        int numBuckets = buckets.length;
        int maxBucketCount = 0;
        int firstPeak = 0;
        int firstPeakSize = 0;
        for (int x = 0; x < numBuckets; x++) {
          if (buckets[x] > firstPeakSize) {
            firstPeak = x;
            firstPeakSize = buckets[x];
          }
          if (buckets[x] > maxBucketCount) {
            maxBucketCount = buckets[x];
          }
        }
    
        // Find the second-tallest peak which is somewhat far from the tallest peak.
        int secondPeak = 0;
        int secondPeakScore = 0;
        for (int x = 0; x < numBuckets; x++) {
          int distanceToBiggest = x - firstPeak;
          // Encourage more distant second peaks by multiplying by square of distance.
          int score = buckets[x] * distanceToBiggest * distanceToBiggest;
          if (score > secondPeakScore) {
            secondPeak = x;
            secondPeakScore = score;
          }
        }
    
        // Make sure firstPeak corresponds to the black peak.
        if (firstPeak > secondPeak) {
          int temp = firstPeak;
          firstPeak = secondPeak;
          secondPeak = temp;
        }
    
        // If there is too little contrast in the image to pick a meaningful black point, throw rather
        // than waste time trying to decode the image, and risk false positives.
        if (secondPeak - firstPeak <= numBuckets / 16) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        // Find a valley between them that is low and closer to the white peak.
        int bestValley = secondPeak - 1;
        int bestValleyScore = -1;
        for (int x = secondPeak - 1; x > firstPeak; x--) {
          int fromFirst = x - firstPeak;
          int score = fromFirst * fromFirst * (secondPeak - x) * (maxBucketCount - buckets[x]);
          if (score > bestValleyScore) {
            bestValley = x;
            bestValleyScore = score;
          }
        }
    
        return bestValley << LUMINANCE_SHIFT;
      }
}
