public class zxing_0177 {

      private static Result translateResultPoints(Result result, int xOffset, int yOffset) {
        ResultPoint[] oldResultPoints = result.getResultPoints();
        if (oldResultPoints == null) {
          return result;
        }
        ResultPoint[] newResultPoints = new ResultPoint[oldResultPoints.length];
        for (int i = 0; i < oldResultPoints.length; i++) {
          ResultPoint oldPoint = oldResultPoints[i];
          if (oldPoint != null) {
            newResultPoints[i] = new ResultPoint(oldPoint.getX() + xOffset, oldPoint.getY() + yOffset);
          }
        }
        Result newResult = new Result(result.getText(),
                                      result.getRawBytes(),
                                      result.getNumBits(),
                                      newResultPoints,
                                      result.getBarcodeFormat(),
                                      result.getTimestamp());
        newResult.putAllMetadata(result.getResultMetadata());
        return newResult;
      }
}
