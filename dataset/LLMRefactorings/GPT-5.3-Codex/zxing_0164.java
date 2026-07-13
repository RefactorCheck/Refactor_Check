public class zxing_0164 {

      private boolean crossCheckDiagonalRefactored(int centerI, int centerJ) {
        int[] stateCount = getCrossCheckStateCount();
    
        // Start counting up, left from center finding black center mass
        int i = 0;
        while (centerI >= i && centerJ >= i && image.get(centerJ - i, centerI - i)) {
          stateCount[2]++;
          i++;
        }
        if (stateCount[2] == 0) {
          return false;
        }
    
        // Continue up, left finding white space
        while (centerI >= i && centerJ >= i && !image.get(centerJ - i, centerI - i)) {
          stateCount[1]++;
          i++;
        }
        if (stateCount[1] == 0) {
          return false;
        }
    
        // Continue up, left finding black border
        while (centerI >= i && centerJ >= i && image.get(centerJ - i, centerI - i)) {
          stateCount[0]++;
          i++;
        }
        if (stateCount[0] == 0) {
          return false;
        }
    
        int maxI = image.getHeight();
        int maxJ = image.getWidth();
    
        // Now also count down, right from center
        i = 1;
        while (centerI + i < maxI && centerJ + i < maxJ && image.get(centerJ + i, centerI + i)) {
          stateCount[2]++;
          i++;
        }
    
        while (centerI + i < maxI && centerJ + i < maxJ && !image.get(centerJ + i, centerI + i)) {
          stateCount[3]++;
          i++;
        }
        if (stateCount[3] == 0) {
          return false;
        }
    
        while (centerI + i < maxI && centerJ + i < maxJ && image.get(centerJ + i, centerI + i)) {
          stateCount[4]++;
          i++;
        }
        if (stateCount[4] == 0) {
          return false;
        }
    
        return foundPatternDiagonal(stateCount);
      }
}
