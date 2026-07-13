public class zxing_0090 {

      Bitmap encodeAsBitmapRefactored() throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
          return null;
        }
        Map<EncodeHintType,Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
          hints = new EnumMap<>(EncodeHintType.class);
          hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result;
        try {
          result = new MultiFormatWriter().encode(contentsToEncode, format, dimension, dimension, hints);
        } catch (IllegalArgumentException iae) {
          // Unsupported format
          return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
          int offset = y * width;
          for (int x = 0; x < width; x++) {
            pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
          }
        }
    
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
      }
}
