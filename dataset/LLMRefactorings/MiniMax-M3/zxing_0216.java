public class zxing_0216 {

      private static BitMatrix renderResult(AztecCode code, int width, int height) {
        BitMatrix input = code.getMatrix();
        if (input == null) {
          throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int outputWidth = Math.max(width, inputWidth);
        int outputHeight = Math.max(height, inputHeight);
    
        int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
    
        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
    
        renderMatrix(input, output, inputWidth, inputHeight, multiple, leftPadding, topPadding);
        return output;
      }
      
      private static void renderMatrix(BitMatrix input, BitMatrix output, int inputWidth, int inputHeight, int multiple, int leftPadding, int topPadding) {
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
          for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
            if (input.get(inputX, inputY)) {
              output.setRegion(outputX, outputY, multiple, multiple);
            }
          }
        }
      }
}
