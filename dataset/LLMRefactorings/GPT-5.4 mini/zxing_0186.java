public class zxing_0186 {

      private boolean isWhiteOrBlackRectangle(Point p1,
                                              Point p2,
                                              Point p3,
                                              Point p4) {
    
        int corrRefactored = 3;
    
        p1 = new Point(Math.max(0, p1.getX() - corrRefactored), Math.min(image.getHeight() - 1, p1.getY() + corrRefactored));
        p2 = new Point(Math.max(0, p2.getX() - corrRefactored), Math.max(0, p2.getY() - corrRefactored));
        p3 = new Point(Math.min(image.getWidth() - 1, p3.getX() + corrRefactored),
                       Math.max(0, Math.min(image.getHeight() - 1, p3.getY() - corrRefactored)));
        p4 = new Point(Math.min(image.getWidth() - 1, p4.getX() + corrRefactored),
                       Math.min(image.getHeight() - 1, p4.getY() + corrRefactored));
    
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
