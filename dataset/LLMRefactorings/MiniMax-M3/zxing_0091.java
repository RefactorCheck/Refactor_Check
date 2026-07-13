public class zxing_0091 {

      @Override
      public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
          throws NotFoundException, ChecksumException, FormatException {
    
        int width = image.getWidth();
        int height = image.getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
    
        try {
          // No need to call makeAbsolute as results will be relative to original top left here
          return delegate.decode(image.crop(0, 0, halfWidth, halfHeight), hints);
        } catch (NotFoundException re) {
          // continue
        }
        try {
          return decodeRegion(image, hints, halfWidth, 0, halfWidth, halfHeight);
        } catch (NotFoundException re) {
          // continue
        }
        try {
          return decodeRegion(image, hints, 0, halfHeight, halfWidth, halfHeight);
        } catch (NotFoundException re) {
          // continue
        }
        try {
          return decodeRegion(image, hints, halfWidth, halfHeight, halfWidth, halfHeight);
        } catch (NotFoundException re) {
          // continue
        }
    
        int quarterWidth = halfWidth / 2;
        int quarterHeight = halfHeight / 2;
        return decodeRegion(image, hints, quarterWidth, quarterHeight, halfWidth, halfHeight);
      }

      private Result decodeRegion(BinaryBitmap image, Map<DecodeHintType,?> hints,
                                  int x, int y, int width, int height)
          throws NotFoundException, ChecksumException, FormatException {
        Result result = delegate.decode(image.crop(x, y, width, height), hints);
        makeAbsolute(result.getResultPoints(), x, y);
        return result;
      }
}
