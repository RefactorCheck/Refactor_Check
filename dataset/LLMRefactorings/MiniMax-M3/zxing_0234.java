public class zxing_0234 {

      @Override
      public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {
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
          processSmallImage(localLuminances, blackPoint, row, width);
        } else {
          processNormalImage(localLuminances, blackPoint, row, width);
        }
        return row;
      }

      private void processSmallImage(byte[] localLuminances, int blackPoint, BitArray row, int width) {
        for (int x = 0; x < width; x++) {
          if ((localLuminances[x] & 0xff) < blackPoint) {
            row.set(x);
          }
        }
      }

      private void processNormalImage(byte[] localLuminances, int blackPoint, BitArray row, int width) {
        int left = localLuminances[0] & 0xff;
        int center = localLuminances[1] & 0xff;
        for (int x = 1; x < width - 1; x++) {
          int right = localLuminances[x + 1] & 0xff;
          if (((center * 4) - left - right) / 2 < blackPoint) {
            row.set(x);
          }
          left = center;
          center = right;
        }
      }
}
