public class zxing_0071 {

      private AlignmentPattern handlePossibleCenter(int[] stateCount, int i, int j) {
        int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
        float centerJ = centerFromEnd(stateCount, j);
        float centerI = crossCheckVertical(i, (int) centerJ, 2 * stateCount[1], stateCountTotal);
        if (!Float.isNaN(centerI)) {
          float estimatedModuleSize = (stateCount[0] + stateCount[1] + stateCount[2]) / 3.0f;
          for (AlignmentPattern center : possibleCenters) {
            // Look for about the same center and module size:
            if (center.aboutEquals(estimatedModuleSize, centerI, centerJ)) {
              return center.combineEstimate(centerI, centerJ, estimatedModuleSize);
            }
          }
          // Hadn't found this before; save it
          AlignmentPattern point = new AlignmentPattern(centerJ, centerI, estimatedModuleSize);
          possibleCenters.add(point);
          if (resultPointCallback != null) {
            resultPointCallback.foundPossibleResultPoint(point);
          }
        }
        return null;
      }
}
