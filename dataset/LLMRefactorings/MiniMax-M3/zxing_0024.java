public class zxing_0024 {

      private static final int HSP_WIDTH = 8;
      private static final int VSP_SIZE = 7;

      private static void embedPositionDetectionPatternsAndSeparators(ByteMatrix matrix) throws WriterException {
        // Embed three big squares at corners.
        int pdpWidth = POSITION_DETECTION_PATTERN[0].length;
        // Left top corner.
        embedPositionDetectionPattern(0, 0, matrix);
        // Right top corner.
        embedPositionDetectionPattern(matrix.getWidth() - pdpWidth, 0, matrix);
        // Left bottom corner.
        embedPositionDetectionPattern(0, matrix.getWidth() - pdpWidth, matrix);
    
        // Embed horizontal separation patterns around the squares.
        // Left top corner.
        embedHorizontalSeparationPattern(0, HSP_WIDTH - 1, matrix);
        // Right top corner.
        embedHorizontalSeparationPattern(matrix.getWidth() - HSP_WIDTH,
            HSP_WIDTH - 1, matrix);
        // Left bottom corner.
        embedHorizontalSeparationPattern(0, matrix.getWidth() - HSP_WIDTH, matrix);
    
        // Embed vertical separation patterns around the squares.
        // Left top corner.
        embedVerticalSeparationPattern(VSP_SIZE, 0, matrix);
        // Right top corner.
        embedVerticalSeparationPattern(matrix.getHeight() - VSP_SIZE - 1, 0, matrix);
        // Left bottom corner.
        embedVerticalSeparationPattern(VSP_SIZE, matrix.getHeight() - VSP_SIZE,
            matrix);
      }
}
