public class zxing_0185 {

      private float crossCheckVertical(int startI, int centerJ, int maxCount,
          int originalStateCountTotal) {
        BitMatrix image = this.image;
    
        int maxI = image.getHeight();
        int[] stateCount = crossCheckStateCount;
        stateCount[0] = 0;
        stateCount[1] = 0;
        stateCount[2] = 0;
    
        // Start counting up from center
        int i = startI;
        while (i >= 0 && image.get(centerJ, i) && stateCount[1] <= maxCount) {
          stateCount[1]++;
          i--;
        }
        // If already too many modules in this state or ran off the edge:
        if (i < 0 || stateCount[1] > maxCount) {
          return Float.NaN;
        }
        while (i >= 0 && !image.get(centerJ, i) && stateCount[0] <= maxCount) {
          stateCount[0]++;
          i--;
        }
        if (stateCount[0] > maxCount) {
          return Float.NaN;
        }
    
        // Now also count down from center
        i = startI + 1;
        while (i < maxI && image.get(centerJ, i) && stateCount[1] <= maxCount) {
          stateCount[1]++;
          i++;
        }
        if (i == maxI || stateCount[1] > maxCount) {
          return Float.NaN;
        }
        while (i < maxI && !image.get(centerJ, i) && stateCount[2] <= maxCount) {
          stateCount[2]++;
          i++;
        }
        if (stateCount[2] > maxCount) {
          return Float.NaN;
        }
    
        int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
        if (5 * Math.abs(stateCountTotal - originalStateCountTotal) >= 2 * originalStateCountTotal) {
          return Float.NaN;
        }
    
        return foundPatternCross(stateCount) ? centerFromEnd(stateCount, i) : Float.NaN;
      }
}
