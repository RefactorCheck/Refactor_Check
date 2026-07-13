public class zxing_0163 {

      protected final AlignmentPattern findAlignmentInRegion(float overallEstModuleSize,
                                                             int estAlignmentX,
                                                             int estAlignmentY,
                                                             float allowanceFactor)
          throws NotFoundException {
        // Look for an alignment pattern (3 modules in size) around where it
        // should be
        int allowance = (int) (allowanceFactor * overallEstModuleSize);
        int[] xRange = computeAlignmentRange(estAlignmentX, allowance, image.getWidth() - 1, overallEstModuleSize);
        int[] yRange = computeAlignmentRange(estAlignmentY, allowance, image.getHeight() - 1, overallEstModuleSize);

        AlignmentPatternFinder alignmentFinder =
            new AlignmentPatternFinder(
                image,
                xRange[0],
                yRange[0],
                xRange[1] - xRange[0],
                yRange[1] - yRange[0],
                overallEstModuleSize,
                resultPointCallback);
        return alignmentFinder.find();
      }

      private int[] computeAlignmentRange(int center, int allowance, int maxDimension, float moduleSize)
          throws NotFoundException {
        int min = Math.max(0, center - allowance);
        int max = Math.min(maxDimension, center + allowance);
        if (max - min < moduleSize * 3) {
          throw NotFoundException.getNotFoundInstance();
        }
        return new int[]{min, max};
      }
}
