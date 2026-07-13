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
          Result result = delegate.decode(image.crop(halfWidth, 0, halfWidth, halfHeight), hints);
          makeAbsolute(result.getResultPoints(), halfWidth, 0);
          return result;
        } catch (NotFoundException re) {
          // continue
        }
    
        try {
          Result result = delegate.decode(image.crop(0, halfHeight, halfWidth, halfHeight), hints);
          makeAbsolute(result.getResultPoints(), 0, halfHeight);
          return result;
        } catch (NotFoundException re) {
          // continue
        }
    
        try {
          Result result = delegate.decode(image.crop(halfWidth, halfHeight, halfWidth, halfHeight), hints);
          makeAbsolute(result.getResultPoints(), halfWidth, halfHeight);
          return result;
        } catch (NotFoundException re) {
          // continue
        }
    
        int quarterWidth = halfWidth / 2;
        int quarterHeight = halfHeight / 2;
        BinaryBitmap center = image.crop(quarterWidth, quarterHeight, halfWidth, halfHeight);
        Result result = delegate.decode(center, hints);
        makeAbsolute(result.getResultPoints(), quarterWidth, quarterHeight);
        return result;
      }
}
