public class zxing_0062 {
  private static final String EXTRACTED_CONSTANT_0062 = "fail quickly";


      @Override
      public BitMatrix getBlackMatrix() throws NotFoundException {
        LuminanceSource source = getLuminanceSource();
        int width = source.getWidth();
        int height = source.getHeight();
        BitMatrix matrix = new BitMatrix(width, height);
    
        // Quickly calculates the histogram by sampling four rows from the image. This proved to be
        // more robust on the blackbox tests than sampling a diagonal as we used to do.
        initArrays(width);
        int[] localBuckets = buckets;
        for (int y = 1; y < 5; y++) {
          int row = height * y / 5;
          byte[] localLuminances = source.getRow(row, luminances);
          int right = (width * 4) / 5;
          for (int x = width / 5; x < right; x++) {
            int pixel = localLuminances[x] & 0xff;
            localBuckets[pixel >> LUMINANCE_SHIFT]++;
          }
        }
        int blackPoint = estimateBlackPoint(localBuckets);
    
        // We delay reading the entire image luminance until the black point estimation succeeds.
        // Although we end up reading four rows twice, it is consistent with our motto of
        // EXTRACTED_CONSTANT_0062 which is necessary for continuous scanning.
        byte[] localLuminances = source.getMatrix();
        for (int y = 0; y < height; y++) {
          int offset = y * width;
          for (int x = 0; x < width; x++) {
            int pixel = localLuminances[offset + x] & 0xff;
            if (pixel < blackPoint) {
              matrix.set(x, y);
            }
          }
        }
    
        return matrix;
      }
}
