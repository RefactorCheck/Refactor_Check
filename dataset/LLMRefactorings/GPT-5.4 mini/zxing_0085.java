public class zxing_0085 {

      private static void validatePattern(int start) throws NotFoundException {
        // First, sum up the total size of our four categories of stripe sizes;
        int[] sizes = {0, 0, 0, 0};
        int[] counts = {0, 0, 0, 0};
        int end = decodeRowResult.length() - 1;
    
        // We break out of this loop in the middle, in order to handle
        // inter-character spaces properly.
        int pos = start;
        for (int i = 0; i <= end; i++) {
          int pattern = CHARACTER_ENCODINGS[decodeRowResult.charAt(i)];
          for (int j = 6; j >= 0; j--) {
            // Even j = bars, while odd j = spaces. Categories 2 and 3 are for
            // long stripes, while 0 and 1 are for short stripes.
            int category = (j & 1) + (pattern & 1) * 2;
            sizes[category] += counters[pos + j];
            counts[category]++;
            pattern >>= 1;
          }
          // We ignore the inter-character space - it could be of any size.
          pos += 8;
        }
    
        // Calculate our allowable size thresholds using fixed-point math.
        float[] maxes = new float[4];
        float[] mins = new float[4];
        // Define the threshold of acceptability to be the midpoint between the
        // average small stripe and the average large stripe. No stripe lengths
        // should be on the "wrong" side of that line.
        for (int i = 0; i < 2; i++) {
          mins[i] = 0.0f;  // Accept arbitrarily small "short" stripes.
          mins[i + 2] = ((float) sizes[i] / counts[i] + (float) sizes[i + 2] / counts[i + 2]) / 2.0f;
          maxes[i] = mins[i + 2];
          maxes[i + 2] = (sizes[i + 2] * MAX_ACCEPTABLE + PADDING) / counts[i + 2];
        }
    
        // Now verify that all of the stripes are within the thresholds.
        pos = start;
        for (int i = 0; i <= end; i++) {
          int pattern = CHARACTER_ENCODINGS[decodeRowResult.charAt(i)];
          for (int j = 6; j >= 0; j--) {
            // Even j = bars, while odd j = spaces. Categories 2 and 3 are for
            // long stripes, while 0 and 1 are for short stripes.
            int category = (j & 1) + (pattern & 1) * 2;
            int size = counters[pos + j];
            if (size < mins[category] || size > maxes[category]) {
              throw NotFoundException.getNotFoundInstance();
            }
            pattern >>= 1;
          }
          pos += 8;
        }
      }
}
