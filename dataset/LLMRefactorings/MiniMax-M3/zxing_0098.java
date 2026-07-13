public class zxing_0098 {

    private static BitMatrix encodeLowLevel(DefaultPlacement placement, SymbolInfo symbolInfo, int width, int height) {
        int symbolWidth = symbolInfo.getSymbolDataWidth();
        int symbolHeight = symbolInfo.getSymbolDataHeight();
        int matrixWidth = symbolInfo.matrixWidth;
        int matrixHeight = symbolInfo.matrixHeight;
        int totalSymbolWidth = symbolInfo.getSymbolWidth();

        ByteMatrix matrix = new ByteMatrix(totalSymbolWidth, symbolInfo.getSymbolHeight());

        int matrixY = 0;

        for (int y = 0; y < symbolHeight; y++) {
            int matrixX;
            if ((y % matrixHeight) == 0) {
                matrixX = 0;
                for (int x = 0; x < totalSymbolWidth; x++) {
                    matrix.set(matrixX, matrixY, (x % 2) == 0);
                    matrixX++;
                }
                matrixY++;
            }
            matrixX = 0;
            for (int x = 0; x < symbolWidth; x++) {
                if ((x % matrixWidth) == 0) {
                    matrix.set(matrixX, matrixY, true);
                    matrixX++;
                }
                matrix.set(matrixX, matrixY, placement.getBit(x, y));
                matrixX++;
                if ((x % matrixWidth) == matrixWidth - 1) {
                    matrix.set(matrixX, matrixY, (y % 2) == 0);
                    matrixX++;
                }
            }
            matrixY++;
            if ((y % matrixHeight) == matrixHeight - 1) {
                matrixX = 0;
                for (int x = 0; x < totalSymbolWidth; x++) {
                    matrix.set(matrixX, matrixY, true);
                    matrixX++;
                }
                matrixY++;
            }
        }

        return convertByteMatrixToBitMatrix(matrix, width, height);
    }
}
