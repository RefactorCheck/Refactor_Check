public class zxing_0291 {

      private static BitMatrix convertByteMatrixToBitMatrix(ByteMatrix matrix, int reqWidth, int reqHeight) {
        int matrixWidth = matrix.getWidth();
        int matrixHeight = matrix.getHeight();
        int outputWidth = Math.max(reqWidth, matrixWidth);
        int outputHeight = Math.max(reqHeight, matrixHeight);
    
        int multiple = Math.min(outputWidth / matrixWidth, outputHeight / matrixHeight);
    
        int leftPadding = (outputWidth - (matrixWidth * multiple)) / 2 ;
        int topPadding = (outputHeight - (matrixHeight * multiple)) / 2 ;
    
        BitMatrix output;
    
        // remove padding if requested width and height are too small
        if (reqHeight < matrixHeight || reqWidth < matrixWidth) {
          leftPadding = 0;
          topPadding = 0;
          output = new BitMatrix(matrixWidth, matrixHeight);
        } else {
          output = new BitMatrix(reqWidth, reqHeight);
        }
    
        output.clear();
        for (int inputY = 0, outputY = topPadding; inputY < matrixHeight; inputY++, outputY += multiple) {
          // Write the contents of this row of the bytematrix
          for (int inputX = 0, outputX = leftPadding; inputX < matrixWidth; inputX++, outputX += multiple) {
            if (matrix.get(inputX, inputY) == 1) {
              output.setRegion(outputX, outputY, multiple, multiple);
            }
          }
        }
    
        return output;
      }
}
