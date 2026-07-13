public class zxing_0166 {

      private static float sizeOfBlackWhiteBlackRun(int fromX, int fromY, int toX, int toY) {
        // Mild variant of Bresenham's algorithm;
        // see http://en.wikipedia.org/wiki/Bresenham's_line_algorithm
        boolean steep = Math.abs(toY - fromY) > Math.abs(toX - fromX);
        if (steep) {
          int temp = fromX;
          fromX = fromY;
          fromY = temp;
          temp = toX;
          toX = toY;
          toY = temp;
        }
    
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        int error = -dx / 2;
        int xstep = fromX < toX ? 1 : -1;
        int ystep = fromY < toY ? 1 : -1;
    
        // In black pixels, looking for white, first or second time.
        int state = 0;
        // Loop up until x == toX, but not beyond
        int xLimit = toX + xstep;
        for (int x = fromX, y = fromY; x != xLimit; x += xstep) {
          int realX = steep ? y : x;
          int realY = steep ? x : y;
    
          // Does current pixel mean we have moved white to black or vice versa?
          // Scanning black in state 0,2 and white in state 1, so if we find the wrong
          // color, advance to next state or end if we are in state 2 already
          if ((state == 1) == image.get(realX, realY)) {
            if (state == 2) {
              return MathUtils.distance(x, y, fromX, fromY);
            }
            state++;
          }
    
          error += dy;
          if (error > 0) {
            if (y == toY) {
              break;
            }
            y += ystep;
            error -= dx;
          }
        }
        // Found black-white-black; give the benefit of the doubt that the next pixel outside the image
        // is "white" so this last point at (toX+xStep,toY) is the right ending. This is really a
        // small approximation; (toX+xStep,toY+yStep) might be really correct. Ignore this.
        if (state == 2) {
          return MathUtils.distance(toX + xstep, toY, fromX, fromY);
        }
        // else we didn't find even black-white-black; no estimate is really possible
        return Float.NaN;
      }
}
