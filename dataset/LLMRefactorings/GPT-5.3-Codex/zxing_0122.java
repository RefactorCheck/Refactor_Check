public class zxing_0122 {

      private boolean haveMultiplyConfirmedCenters(boolean normalize) {
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
        // OK, we have at least 3 confirmed centers, but, it's possible that one is a "false positive"
        // and that we need to keep looking. We detect this by asking if the estimated module sizes
        // vary too much. We arbitrarily say that when the total deviation from average exceeds
        // 5% of the total module size estimates, it's too much.
        float average = totalModuleSize / max;
        float totalDeviation = 0.0f;
        for (FinderPattern pattern : possibleCenters) {
          totalDeviation += Math.abs(pattern.getEstimatedModuleSize() - average);
        }
        return totalDeviation <= 0.05f * totalModuleSize;
      }
}
