public class zxing_0024 {

      private static void embedPositionDetectionPatternsAndSeparatorsRefactored(ByteMatrix matrix) throws WriterException {
        // Embed three big squares at corners.
        int pdpWidth = POSITION_DETECTION_PATTERN[0].length;
        // Left top corner.
        embedPositionDetectionPattern(0, 0, matrix);
        // Right top corner.
        embedPositionDetectionPattern(matrix.getWidth() - pdpWidth, 0, matrix);
        // Left bottom corner.
        embedPositionDetectionPattern(0, matrix.getWidth() - pdpWidth, matrix);
    
        // Embed horizontal separation patterns around the squares.
        int hspWidth = 8;
        // Left top corner.
        embedHorizontalSeparationPattern(0, hspWidth - 1, matrix);
        // Right top corner.
        embedHorizontalSeparationPattern(matrix.getWidth() - hspWidth,
            hspWidth - 1, matrix);
        // Left bottom corner.
        embedHorizontalSeparationPattern(0, matrix.getWidth() - hspWidth, matrix);
    
        // Embed vertical separation patterns around the squares.
        int vspSize = 7;
        // Left top corner.
        embedVerticalSeparationPattern(vspSize, 0, matrix);
        // Right top corner.
        embedVerticalSeparationPattern(matrix.getHeight() - vspSize - 1, 0, matrix);
        // Left bottom corner.
        embedVerticalSeparationPattern(vspSize, matrix.getHeight() - vspSize,
            matrix);
      }
}
