public class zxing_0164 {

      private boolean crossCheckDiagonal(int centerI, int centerJ) {
        int[] stateCount = getCrossCheckStateCount();
    
        int i = 0;
        i = countAlongDiagonal(centerI, centerJ, i, -1, true, stateCount, 2);
        if (stateCount[2] == 0) {
          return false;
        }
    
        i = countAlongDiagonal(centerI, centerJ, i, -1, false, stateCount, 1);
        if (stateCount[1] == 0) {
          return false;
        }
    
        i = countAlongDiagonal(centerI, centerJ, i, -1, true, stateCount, 0);
        if (stateCount[0] == 0) {
          return false;
        }
    
        int maxI = image.getHeight();
        int maxJ = image.getWidth();
    
        i = 1;
        i = countAlongDiagonal(centerI, centerJ, i, 1, true, stateCount, 2);
    
        i = countAlongDiagonal(centerI, centerJ, i, 1, false, stateCount, 3);
        if (stateCount[3] == 0) {
          return false;
        }
    
        i = countAlongDiagonal(centerI, centerJ, i, 1, true, stateCount, 4);
        if (stateCount[4] == 0) {
          return false;
        }
    
        return foundPatternDiagonal(stateCount);
      }
    
      private int countAlongDiagonal(int centerI, int centerJ, int startI, int step, boolean targetValue, int[] stateCount, int stateIndex) {
        int maxI = image.getHeight();
        int maxJ = image.getWidth();
        int i = startI;
        while (centerI + step * i >= 0 && centerI + step * i < maxI && 
               centerJ + step * i >= 0 && centerJ + step * i < maxJ && 
               image.get(centerJ + step * i, centerI + step * i) == targetValue) {
          stateCount[stateIndex]++;
          i++;
        }
        return i;
      }
}
