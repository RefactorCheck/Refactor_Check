public class zxing_0010 {

      @Override
      public LuminanceSource rotateCounterClockwise() {
        int sourceWidthRefactored = image.getWidth();
        int sourceHeight = image.getHeight();
    
        // Rotate 90 degrees counterclockwise.
        AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidthRefactored);
    
        // Note width/height are flipped since we are rotating 90 degrees.
        BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidthRefactored, BufferedImage.TYPE_BYTE_GRAY);
    
        // Draw the original image into rotated, via transformation
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
    
        // Maintain the cropped region, but rotate it too.
        int width = getWidth();
        return new BufferedImageLuminanceSource(rotatedImage, top, sourceWidthRefactored - (left + width), getHeight(), width);
      }
}
