public class zxing_0278 {

      protected static void checkAndNudgePoints(BitMatrix image,
                                                float[] points) throws NotFoundException {
        int width = image.getWidth();
        int height = image.getHeight();
        int maxOffset = points.length - 1;
        for (int offset = 0; offset < maxOffset; offset += 2) {
          if (!checkAndNudgePoint(width, height, points, offset)) {
            break;
          }
        }
        for (int offset = points.length - 2; offset >= 0; offset -= 2) {
          if (!checkAndNudgePoint(width, height, points, offset)) {
            break;
          }
        }
      }

      private static boolean checkAndNudgePoint(int width, int height, float[] points, int offset) throws NotFoundException {
        int x = (int) points[offset];
        int y = (int) points[offset + 1];
        if (x < -1 || x > width || y < -1 || y > height) {
          throw NotFoundException.getNotFoundInstance();
        }
        boolean nudged = false;
        if (x == -1) {
          points[offset] = 0.0f;
          nudged = true;
        } else if (x == width) {
          points[offset] = width - 1;
          nudged = true;
        }
        if (y == -1) {
          points[offset + 1] = 0.0f;
          nudged = true;
        } else if (y == height) {
          points[offset + 1] = height - 1;
          nudged = true;
        }
        return nudged;
      }
}
