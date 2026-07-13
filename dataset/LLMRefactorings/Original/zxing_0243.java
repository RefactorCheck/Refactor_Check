public class zxing_0243 {

      private int getColor(Point p1, Point p2) {
        float d = distance(p1, p2);
        if (d == 0.0f) {
          return 0;
        }
        float dx = (p2.getX() - p1.getX()) / d;
        float dy = (p2.getY() - p1.getY()) / d;
        int error = 0;
    
        float px = p1.getX();
        float py = p1.getY();
    
        boolean colorModel = image.get(p1.getX(), p1.getY());
    
        int iMax = (int) Math.floor(d);
        for (int i = 0; i < iMax; i++) {
          if (image.get(MathUtils.round(px), MathUtils.round(py)) != colorModel) {
            error++;
          }
          px += dx;
          py += dy;
        }
    
        float errRatio = error / d;
    
        if (errRatio > 0.1f && errRatio < 0.9f) {
          return 0;
        }
    
        return (errRatio <= 0.1f) == colorModel ? 1 : -1;
      }
}
