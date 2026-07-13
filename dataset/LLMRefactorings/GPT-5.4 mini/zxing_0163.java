public class zxing_0163 {

      protected static final AlignmentPattern findAlignmentInRegion(float overallEstModuleSize,
                                                             int estAlignmentX,
                                                             int estAlignmentY,
                                                             float allowanceFactor)
          throws NotFoundException {
        // Look for an alignment pattern (3 modules in size) around where it
        // should be
        int allowance = (int) (allowanceFactor * overallEstModuleSize);
        int alignmentAreaLeftX = Math.max(0, estAlignmentX - allowance);
        int alignmentAreaRightX = Math.min(image.getWidth() - 1, estAlignmentX + allowance);
        if (alignmentAreaRightX - alignmentAreaLeftX < overallEstModuleSize * 3) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        int alignmentAreaTopY = Math.max(0, estAlignmentY - allowance);
        int alignmentAreaBottomY = Math.min(image.getHeight() - 1, estAlignmentY + allowance);
        if (alignmentAreaBottomY - alignmentAreaTopY < overallEstModuleSize * 3) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        AlignmentPatternFinder alignmentFinder =
            new AlignmentPatternFinder(
                image,
                alignmentAreaLeftX,
                alignmentAreaTopY,
                alignmentAreaRightX - alignmentAreaLeftX,
                alignmentAreaBottomY - alignmentAreaTopY,
                overallEstModuleSize,
                resultPointCallback);
        return alignmentFinder.find();
      }
}
