public class zxing_0185 {

      private float crossCheckVertical(int startI, int centerJ, int maxCount,
          int originalStateCountTotal) {
        BitMatrix image = this.image;
    
        int maxI = image.getHeight();
        int[] stateCount = crossCheckStateCount;
        stateCount[0] = 0;
        stateCount[1] = 0;
        stateCount[2] = 0;
    
        int i = scan(startI, centerJ, true, maxI, maxCount, stateCount, 1, -1);
        if (i < 0 || stateCount[1] > maxCount) {
          return Float.NaN;
        }
        i = scan(i, centerJ, false, maxI, maxCount, stateCount, 0, -1);
        if (i < 0 || stateCount[0] > maxCount) {
          return Float.NaN;
        }
    
        i = scan(startI + 1, centerJ, true, maxI, maxCount, stateCount, 1, 1);
        if (i == maxI || stateCount[1] > maxCount) {
          return Float.NaN;
        }
        i = scan(i, centerJ, false, maxI, maxCount, stateCount, 2, 1);
        if (i == maxI || stateCount[2] > maxCount) {
          return Float.NaN;
        }
    
        int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
        if (5 * Math.abs(stateCountTotal - originalStateCountTotal) >= 2 * originalStateCountTotal) {
          return Float.NaN;
        }
    
        return foundPatternCross(stateCount) ? centerFromEnd(stateCount, i) : Float.NaN;
      }

      private int scan(int startI, int centerJ, boolean target, int maxI, int maxCount,
          int[] stateCount, int stateCountIdx, int direction) {
        int i = startI;
        while (i >= 0 && i < maxI && image.get(centerJ, i) == target && stateCount[stateCountIdx] <= maxCount) {
          stateCount[stateCountIdx]++;
          i += direction;
        }
        return i;
      }
}
