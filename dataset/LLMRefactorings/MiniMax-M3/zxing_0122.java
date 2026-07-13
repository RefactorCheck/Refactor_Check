public class zxing_0122 {

      private boolean haveMultiplyConfirmedCenters() {
        int confirmedCount = 0;
        float totalModuleSize = 0.0f;
        int max = possibleCenters.size();
        for (FinderPattern pattern : possibleCenters) {
          if (pattern.getCount() >= CENTER_QUORUM) {
            confirmedCount++;
            totalModuleSize += pattern.getEstimatedModuleSize();
          }
        }
        if (confirmedCount < 3) {
          return false;
        }
        float average = totalModuleSize / max;
        float totalDeviation = getTotalDeviation(average);
        return totalDeviation <= 0.05f * totalModuleSize;
      }

      private float getTotalDeviation(float average) {
        float totalDeviation = 0.0f;
        for (FinderPattern pattern : possibleCenters) {
          totalDeviation += Math.abs(pattern.getEstimatedModuleSize() - average);
        }
        return totalDeviation;
      }
}
