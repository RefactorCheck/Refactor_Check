public class zxing_0195 {

    private static final int CORNER_OFFSET = 7;

    private Point getMatrixCenter() {

        ResultPoint pointA;
        ResultPoint pointB;
        ResultPoint pointC;
        ResultPoint pointD;

        try {

            ResultPoint[] cornerPoints = new WhiteRectangleDetector(image).detect();
            pointA = cornerPoints[0];
            pointB = cornerPoints[1];
            pointC = cornerPoints[2];
            pointD = cornerPoints[3];

        } catch (NotFoundException e) {

            int cx = image.getWidth() / 2;
            int cy = image.getHeight() / 2;
            pointA = getFirstDifferent(new Point(cx + CORNER_OFFSET, cy - CORNER_OFFSET), false, 1, -1).toResultPoint();
            pointB = getFirstDifferent(new Point(cx + CORNER_OFFSET, cy + CORNER_OFFSET), false, 1, 1).toResultPoint();
            pointC = getFirstDifferent(new Point(cx - CORNER_OFFSET, cy + CORNER_OFFSET), false, -1, 1).toResultPoint();
            pointD = getFirstDifferent(new Point(cx - CORNER_OFFSET, cy - CORNER_OFFSET), false, -1, -1).toResultPoint();

        }

        int cx = MathUtils.round((pointA.getX() + pointD.getX() + pointB.getX() + pointC.getX()) / 4.0f);
        int cy = MathUtils.round((pointA.getY() + pointD.getY() + pointB.getY() + pointC.getY()) / 4.0f);

        try {
            ResultPoint[] cornerPoints = new WhiteRectangleDetector(image, 15, cx, cy).detect();
            pointA = cornerPoints[0];
            pointB = cornerPoints[1];
            pointC = cornerPoints[2];
            pointD = cornerPoints[3];
        } catch (NotFoundException e) {
            pointA = getFirstDifferent(new Point(cx + CORNER_OFFSET, cy - CORNER_OFFSET), false, 1, -1).toResultPoint();
            pointB = getFirstDifferent(new Point(cx + CORNER_OFFSET, cy + CORNER_OFFSET), false, 1, 1).toResultPoint();
            pointC = getFirstDifferent(new Point(cx - CORNER_OFFSET, cy + CORNER_OFFSET), false, -1, 1).toResultPoint();
            pointD = getFirstDifferent(new Point(cx - CORNER_OFFSET, cy - CORNER_OFFSET), false, -1, -1).toResultPoint();
        }

        cx = MathUtils.round((pointA.getX() + pointD.getX() + pointB.getX() + pointC.getX()) / 4.0f);
        cy = MathUtils.round((pointA.getY() + pointD.getY() + pointB.getY() + pointC.getY()) / 4.0f);

        return new Point(cx, cy);
    }
}
