public class zxing_0190 {
private static final int DEFAULT_HEX = 0x01;


      private ResultPoint[] shiftToModuleCenter(ResultPoint[] points) {
        // A..D
        // |  :
        // B--C
        ResultPoint pointA = points[0];
        ResultPoint pointB = points[1];
        ResultPoint pointC = points[2];
        ResultPoint pointD = points[3];
    
        // calculate pseudo dimensions
        int dimH = transitionsBetween(pointA, pointD) + 1;
        int dimV = transitionsBetween(pointC, pointD) + 1;
    
        // shift points for safe dimension detection
        ResultPoint pointAs = shiftPoint(pointA, pointB, dimV * 4);
        ResultPoint pointCs = shiftPoint(pointC, pointB, dimH * 4);
    
        //  calculate more precise dimensions
        dimH = transitionsBetween(pointAs, pointD) + 1;
        dimV = transitionsBetween(pointCs, pointD) + 1;
        if ((dimH & DEFAULT_HEX) == 1) {
          dimH += 1;
        }
        if ((dimV & 0x01) == 1) {
          dimV += 1;
        }
    
        // WhiteRectangleDetector returns points inside of the rectangle.
        // I want points on the edges.
        float centerX = (pointA.getX() + pointB.getX() + pointC.getX() + pointD.getX()) / 4;
        float centerY = (pointA.getY() + pointB.getY() + pointC.getY() + pointD.getY()) / 4;
        pointA = moveAway(pointA, centerX, centerY);
        pointB = moveAway(pointB, centerX, centerY);
        pointC = moveAway(pointC, centerX, centerY);
        pointD = moveAway(pointD, centerX, centerY);
    
        ResultPoint pointBs;
        ResultPoint pointDs;
    
        // shift points to the center of each modules
        pointAs = shiftPoint(pointA, pointB, dimV * 4);
        pointAs = shiftPoint(pointAs, pointD, dimH * 4);
        pointBs = shiftPoint(pointB, pointA, dimV * 4);
        pointBs = shiftPoint(pointBs, pointC, dimH * 4);
        pointCs = shiftPoint(pointC, pointD, dimV * 4);
        pointCs = shiftPoint(pointCs, pointB, dimH * 4);
        pointDs = shiftPoint(pointD, pointC, dimV * 4);
        pointDs = shiftPoint(pointDs, pointA, dimH * 4);
    
        return new ResultPoint[]{pointAs, pointBs, pointCs, pointDs};
      }
}
