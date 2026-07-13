public class zxing_0064 {

    private static List<ResultPoint[]> detect(boolean multiple, BitMatrix bitMatrix) {
        List<ResultPoint[]> barcodeCoordinates = new ArrayList<>();
        int row = 0;
        int column = 0;
        boolean foundBarcodeInRow = false;
        while (row < bitMatrix.getHeight()) {
            ResultPoint[] vertices = findVertices(bitMatrix, row, column);

            if (vertices[0] == null && vertices[3] == null) {
                if (!foundBarcodeInRow) {
                    break;
                }
                foundBarcodeInRow = false;
                column = 0;
                row = findNextRowToSearch(barcodeCoordinates, row);
                row += ROW_STEP;
                continue;
            }
            foundBarcodeInRow = true;
            barcodeCoordinates.add(vertices);
            if (!multiple) {
                break;
            }
            if (vertices[2] != null) {
                column = (int) vertices[2].getX();
                row = (int) vertices[2].getY();
            } else {
                column = (int) vertices[4].getX();
                row = (int) vertices[4].getY();
            }
        }
        return barcodeCoordinates;
    }

    private static int findNextRowToSearch(List<ResultPoint[]> barcodeCoordinates, int row) {
        for (ResultPoint[] barcodeCoordinate : barcodeCoordinates) {
            if (barcodeCoordinate[1] != null) {
                row = (int) Math.max(row, barcodeCoordinate[1].getY());
            }
            if (barcodeCoordinate[3] != null) {
                row = Math.max(row, (int) barcodeCoordinate[3].getY());
            }
        }
        return row;
    }
}
