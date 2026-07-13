public class zxing_0059 {

      private Result decodeInternal(BinaryBitmap image) throws NotFoundException {
        if (readers != null) {
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
          if (hints != null && hints.containsKey(DecodeHintType.ALSO_INVERTED)) {
            // Calling all readers again with inverted image
            image.getBlackMatrix().flip();
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
          }
        }
        throw NotFoundException.getNotFoundInstance();
      }
}
