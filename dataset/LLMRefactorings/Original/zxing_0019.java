public class zxing_0019 {

      private ResultPoint[] getBullsEyeCorners(Point pCenter) throws NotFoundException {
    
        Point pina = pCenter;
        Point pinb = pCenter;
        Point pinc = pCenter;
        Point pind = pCenter;
    
        boolean color = true;
    
        for (nbCenterLayers = 1; nbCenterLayers < 9; nbCenterLayers++) {
          Point pouta = getFirstDifferent(pina, color, 1, -1);
          Point poutb = getFirstDifferent(pinb, color, 1, 1);
          Point poutc = getFirstDifferent(pinc, color, -1, 1);
          Point poutd = getFirstDifferent(pind, color, -1, -1);
    
          //d      a
          //
          //c      b
    
          if (nbCenterLayers > 2) {
            float q = distance(poutd, pouta) * nbCenterLayers / (distance(pind, pina) * (nbCenterLayers + 2));
            if (q < 0.75 || q > 1.25 || !isWhiteOrBlackRectangle(pouta, poutb, poutc, poutd)) {
              break;
            }
          }
    
          pina = pouta;
          pinb = poutb;
          pinc = poutc;
          pind = poutd;
    
          color = !color;
        }
    
        if (nbCenterLayers != 5 && nbCenterLayers != 7) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        compact = nbCenterLayers == 5;
    
        // Expand the square by .5 pixel in each direction so that we're on the border
        // between the white square and the black square
        ResultPoint pinax = new ResultPoint(pina.getX() + 0.5f, pina.getY() - 0.5f);
        ResultPoint pinbx = new ResultPoint(pinb.getX() + 0.5f, pinb.getY() + 0.5f);
        ResultPoint pincx = new ResultPoint(pinc.getX() - 0.5f, pinc.getY() + 0.5f);
        ResultPoint pindx = new ResultPoint(pind.getX() - 0.5f, pind.getY() - 0.5f);
    
        // Expand the square so that its corners are the centers of the points
        // just outside the bull's eye.
        return expandSquare(new ResultPoint[]{pinax, pinbx, pincx, pindx},
                            2 * nbCenterLayers - 3,
                            2 * nbCenterLayers);
      }
}
