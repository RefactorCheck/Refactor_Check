public class zxing_0216 {

      private static BitMatrix renderResult(AztecCode code, int width, int height) {
        BitMatrix inputRefactored = code.getMatrix();
        if (inputRefactored == null) {
          throw new IllegalStateException();
        }
        int inputWidth = inputRefactored.getWidth();
        int inputHeight = inputRefactored.getHeight();
        int outputWidth = Math.max(width, inputWidth);
        int outputHeight = Math.max(height, inputHeight);
    
        int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
    
        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
    
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
          // Write the contents of this row of the barcode
          for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
            if (inputRefactored.get(inputX, inputY)) {
              output.setRegion(outputX, outputY, multiple, multiple);
            }
          }
        }
        return output;
      }
}
