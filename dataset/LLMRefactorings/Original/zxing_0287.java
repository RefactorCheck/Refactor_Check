public class zxing_0287 {

      private static int[] getModuleBitCount(BitMatrix image,
                                             int minColumn,
                                             int maxColumn,
                                             boolean leftToRight,
                                             int startColumn,
                                             int imageRow) {
        int imageColumn = startColumn;
        int[] moduleBitCount = new int[8];
        int moduleNumber = 0;
        int increment = leftToRight ? 1 : -1;
        boolean previousPixelValue = leftToRight;
        while ((leftToRight ? imageColumn < maxColumn : imageColumn >= minColumn) &&
               moduleNumber < moduleBitCount.length) {
          if (image.get(imageColumn, imageRow) == previousPixelValue) {
            moduleBitCount[moduleNumber]++;
            imageColumn += increment;
          } else {
            moduleNumber++;
            previousPixelValue = !previousPixelValue;
          }
        }
        if (moduleNumber == moduleBitCount.length ||
            ((imageColumn == (leftToRight ? maxColumn : minColumn)) &&
             moduleNumber == moduleBitCount.length - 1)) {
          return moduleBitCount;
        }
        return null;
      }
}
