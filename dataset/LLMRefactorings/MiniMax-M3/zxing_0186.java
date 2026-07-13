public class zxing_0186 {

      private boolean isWhiteOrBlackRectangle(Point p1,
                                              Point p2,
                                              Point p3,
                                              Point p4) {
    
        int corr = 3;
        int maxX = image.getWidth() - 1;
        int maxY = image.getHeight() - 1;
    
        p1 = new Point(Math.max(0, p1.getX() - corr), Math.min(maxY, p1.getY() + corr));
        p2 = new Point(Math.max(0, p2.getX() - corr), Math.max(0, p2.getY() - corr));
        p3 = new Point(Math.min(maxX, p3.getX() + corr),
                       Math.max(0, Math.min(maxY, p3.getY() - corr)));
        p4 = new Point(Math.min(maxX, p4.getX() + corr),
                       Math.min(maxY, p4.getY() + corr));
    
        int cInit = getColor(p4, p1);
    
        if (cInit == 0) {
          return false;
        }
    
        int c = getColor(p1, p2);
    
        if (c != cInit) {
          return false;
        }
    
        c = getColor(p2, p3);
    
        if (c != cInit) {
          return false;
        }
    
        c = getColor(p3, p4);
    
        return c == cInit;
    
      }
}
