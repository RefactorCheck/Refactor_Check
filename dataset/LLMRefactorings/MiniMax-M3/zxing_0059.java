public class zxing_0059 {

      private Result decodeInternal(BinaryBitmap image) throws NotFoundException {
        if (readers != null) {
          Result result = decode(image);
          if (result == null && hints != null && hints.containsKey(DecodeHintType.ALSO_INVERTED)) {
            // Calling all readers again with inverted image
            image.getBlackMatrix().flip();
            result = decode(image);
          }
          if (result != null) {
            return result;
          }
        }
        throw NotFoundException.getNotFoundInstance();
      }

      private Result decode(BinaryBitmap image) throws NotFoundException {
        for (Reader reader : readers) {
          if (Thread.currentThread().isInterrupted()) {
            throw NotFoundException.getNotFoundInstance();
          }
          try {
            return reader.decode(image, hints);
          } catch (ReaderException re) {
            // continue
          }
        }
        return null;
      }
}
