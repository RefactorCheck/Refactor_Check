public class zxing_0071 {

      private AlignmentPattern findMatchingCenter(float centerI, float centerJ, float estimatedModuleSize) {
        for (AlignmentPattern center : possibleCenters) {
          if (center.aboutEquals(estimatedModuleSize, centerI, centerJ)) {
            return center.combineEstimate(centerI, centerJ, estimatedModuleSize);
          }
        }
        return null;
      }

      private AlignmentPattern handlePossibleCenter(int[] stateCount, int i, int j) {
        int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
        float centerJ = centerFromEnd(stateCount, j);
        float centerI = crossCheckVertical(i, (int) centerJ, 2 * stateCount[1], stateCountTotal);
        if (!Float.isNaN(centerI)) {
          float estimatedModuleSize = stateCountTotal / 3.0f;
          AlignmentPattern combined = findMatchingCenter(centerI, centerJ, estimatedModuleSize);
          if (combined != null) {
            return combined;
          }
          AlignmentPattern point = new AlignmentPattern(centerJ, centerI, estimatedModuleSize);
          possibleCenters.add(point);
          if (resultPointCallback != null) {
            resultPointCallback.foundPossibleResultPoint(point);
          }
        }
        return null;
      }
}
