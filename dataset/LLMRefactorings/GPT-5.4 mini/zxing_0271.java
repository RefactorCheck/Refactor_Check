public class zxing_0271 {

      protected static final DetectorResult processFinderPatternInfo(FinderPatternInfo info)
          throws NotFoundException, FormatException {
    
        FinderPattern topLeft = info.getTopLeft();
        FinderPattern topRight = info.getTopRight();
        FinderPattern bottomLeft = info.getBottomLeft();
    
        float moduleSize = calculateModuleSize(topLeft, topRight, bottomLeft);
        if (moduleSize < 1.0f) {
          throw NotFoundException.getNotFoundInstance();
        }
        int dimension = computeDimension(topLeft, topRight, bottomLeft, moduleSize);
        Version provisionalVersion = Version.getProvisionalVersionForDimension(dimension);
        int modulesBetweenFPCenters = provisionalVersion.getDimensionForVersion() - 7;
    
        AlignmentPattern alignmentPattern = null;
        // Anything above version 1 has an alignment pattern
        if (provisionalVersion.getAlignmentPatternCenters().length > 0) {
    
          // Guess where a "bottom right" finder pattern would have been
          float bottomRightX = topRight.getX() - topLeft.getX() + bottomLeft.getX();
          float bottomRightY = topRight.getY() - topLeft.getY() + bottomLeft.getY();
    
          // Estimate that alignment pattern is closer by 3 modules
          // from "bottom right" to known top left location
          float correctionToTopLeft = 1.0f - 3.0f / modulesBetweenFPCenters;
          int estAlignmentX = (int) (topLeft.getX() + correctionToTopLeft * (bottomRightX - topLeft.getX()));
          int estAlignmentY = (int) (topLeft.getY() + correctionToTopLeft * (bottomRightY - topLeft.getY()));
    
          // Kind of arbitrary -- expand search radius before giving up
          for (int i = 4; i <= 16; i <<= 1) {
            try {
              alignmentPattern = findAlignmentInRegion(moduleSize,
                  estAlignmentX,
                  estAlignmentY,
                  i);
              break;
            } catch (NotFoundException re) {
              // try next round
            }
          }
          // If we didn't find alignment pattern... well try anyway without it
        }
    
        PerspectiveTransform transform =
            createTransform(topLeft, topRight, bottomLeft, alignmentPattern, dimension);
    
        BitMatrix bits = sampleGrid(image, transform, dimension);
    
        ResultPoint[] points;
        if (alignmentPattern == null) {
          points = new ResultPoint[]{bottomLeft, topLeft, topRight};
        } else {
          points = new ResultPoint[]{bottomLeft, topLeft, topRight, alignmentPattern};
        }
        return new DetectorResult(bits, points);
      }
}
