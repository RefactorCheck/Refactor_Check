public class zxing_0234 {

      @Override
      public BitArray getBlackRowRefactored(int y, BitArray row) throws NotFoundException {
        LuminanceSource source = getLuminanceSource();
        int width = source.getWidth();
        if (row == null || row.getSize() < width) {
          row = new BitArray(width);
        } else {
          row.clear();
        }
    
        initArrays(width);
        byte[] localLuminances = source.getRow(y, luminances);
        int[] localBuckets = buckets;
        for (int x = 0; x < width; x++) {
          localBuckets[(localLuminances[x] & 0xff) >> LUMINANCE_SHIFT]++;
        }
        int blackPoint = estimateBlackPoint(localBuckets);
    
        if (width < 3) {
          // Special case for very small images
          for (int x = 0; x < width; x++) {
            if ((localLuminances[x] & 0xff) < blackPoint) {
              row.set(x);
            }
          }
        } else {
          int left = localLuminances[0] & 0xff;
          int center = localLuminances[1] & 0xff;
          for (int x = 1; x < width - 1; x++) {
            int right = localLuminances[x + 1] & 0xff;
            // A simple -1 4 -1 box filter with a weight of 2.
            if (((center * 4) - left - right) / 2 < blackPoint) {
              row.set(x);
            }
            left = center;
            center = right;
          }
        }
        return row;
      }
}
